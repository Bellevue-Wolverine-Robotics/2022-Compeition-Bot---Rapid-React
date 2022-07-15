package frc.robot;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ArcadeDriveDistanceCommand;
import frc.robot.commands.ArcadeDriveTurnCommand;
import frc.robot.commands.ArcadeDriveUntilCloseCommand;
import frc.robot.commands.IntakeArmToggleCommand;
import frc.robot.commands.IntakeReverseCommand;
import frc.robot.commands.IntakeStartCommand;
import frc.robot.commands.IntakeStopCommand;
import frc.robot.subsystems.DriveTrainSubsystem;

public class AutonomousManager {
    private final boolean m_runPathPlanTest = true;

    private RobotMap m_robotMap;

    private DigitalInput m_switch1 = new DigitalInput(0);

    private Command m_currentCommands;

    public AutonomousManager(RobotMap robotMap) {
        this.m_robotMap = robotMap;
    }

    private double m_motorSpeed;
    private double m_intakeReverseTimeToScore;
    private double m_distanceToDriveAfterIntakeMotorSpeedDrop;
    private double m_turnAroundAngle;
    private double m_distanceFromHubToScore;
    private double m_reverseAfterFinished;
    private double m_turnAroundAfterScoreAngle;

    // Please read about command groups (https://docs.wpilib.org/en/stable/docs/software/commandbased/command-groups.html)
    // to understand how the plans are being made
    public void autonomousInit() {
        this.m_currentCommands = getAutonomousCommand();

        // Start plan along with calibration
        this.m_currentCommands.schedule(false);
    }

    public void autonomousPeriodic() {

    }

    private Command getAutonomousCommand() {
        if (this.m_runPathPlanTest) {
            return getPathPlanningAutonomousCommand();
        } else {
            // Define variables up here so we can change them easily
            // Motor speed drop will be controlled in IntakeSubsystem
            
            this.m_motorSpeed = SmartDashboard.getNumber("motorSpeed", 0.4);                                                          // This is in percent
            this.m_intakeReverseTimeToScore = SmartDashboard.getNumber("intakeReverseTimeToScore", 2);                                // This is in seconds
            this.m_distanceToDriveAfterIntakeMotorSpeedDrop = SmartDashboard.getNumber("distanceToDriveAfterIntakeMotorSpeedDrop", 2);// This is in inches
            this.m_turnAroundAngle = SmartDashboard.getNumber("turnAroundAngle", 171);                                                // This is in degrees
            this.m_distanceFromHubToScore = SmartDashboard.getNumber("distanceFromHubToScore", 12);                                   // This is in inches
            this.m_reverseAfterFinished = SmartDashboard.getNumber("reverseAfterFinished", 120);                                      // This is in inches
            this.m_turnAroundAfterScoreAngle = SmartDashboard.getNumber("turnAroundAfterScoreAngle", 180);                            // This is in degrees
            
            // This if statement can be change (actually please change it) once we know
            // more about how we're going to control the different auto plans
            if (this.m_switch1.get()) {
                return get1BallAutonomousCommand();
            } else {
                return get2BallAutonomousCommand();
            }
        }
    }

    private Command get1BallAutonomousCommand() {
        return new SequentialCommandGroup(
            // Since we start just outside of lower hopper, just spit out ball
            // The parallel command group will just ensure the intake is run for the right amount of time
            new ParallelCommandGroup(
                new IntakeReverseCommand(this.m_robotMap.getIntake()),
                new WaitCommand(m_intakeReverseTimeToScore)    
            ).andThen(() -> System.out.println("finished scoring")),

            // Stop the intake
            new IntakeStopCommand(this.m_robotMap.getIntake()).andThen(() -> System.out.println("finished stopped intake")),
                            
            // Reverse away from the cart
            new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 28, -m_motorSpeed),
            
            // flip a 180, lower the hopper and drive away
            new ArcadeDriveTurnCommand(this.m_robotMap, m_turnAroundAfterScoreAngle, m_motorSpeed).andThen(() -> System.out.println("finished turn around")),
            new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), m_reverseAfterFinished, m_motorSpeed).andThen(() -> System.out.println("finished back away"))
        );
    } 

    private Command get2BallAutonomousCommand() {
        return new SequentialCommandGroup(
            // turn on intake 
            new IntakeStartCommand(this.m_robotMap.getIntake()),

            // Move hopper down
            new IntakeArmToggleCommand(this.m_robotMap.getIntake()),
            // Parallel command group will run all these commands at the same time and stop once one finishes
            // move forward, until our motor speed drops or 4 seconds passes
            new ParallelRaceGroup(

                // This command *should* get interrupted, but it will stop after 5 feet for safety
                new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 60, m_motorSpeed),
                new WaitCommand(4)

            ).withInterrupt(() -> {
                // This withInterrupt() call will stop the parallel command group when
                // this lambda returns true

                // Stop the command once we've intaked the ball
                return this.m_robotMap.getIntake().hasIntakedBall();
            }).andThen(() -> System.out.println("finished intake ball")),

            // Drive 2 more inches then raise and stop the intake and turn around 170 degrees
            new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), m_distanceToDriveAfterIntakeMotorSpeedDrop, m_motorSpeed).andThen(() -> System.out.println("finished drive 2 more inches")),
            new IntakeStopCommand(this.m_robotMap.getIntake()),
            new IntakeArmToggleCommand(this.m_robotMap.getIntake()), 
            new ArcadeDriveTurnCommand(this.m_robotMap, m_turnAroundAngle, m_motorSpeed).andThen(() -> System.out.println("finished turn")),

            // Drive to up until within XX inches
            new ArcadeDriveUntilCloseCommand(this.m_robotMap, m_distanceFromHubToScore, m_motorSpeed).andThen(() -> System.out.println("finished drive until close")),
            
            // Reverse intake motors and stop them
            new ParallelCommandGroup(
                new IntakeReverseCommand(this.m_robotMap.getIntake()),
                new WaitCommand(m_intakeReverseTimeToScore)                    
            ).andThen(() -> System.out.println("finished scoring")),
            new IntakeStopCommand(this.m_robotMap.getIntake()).andThen(() -> System.out.println("finished stop intake")),

            // Reverse away from the cart
            new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 28, -m_motorSpeed),

            // flip a 180, lower the hopper and drive away
            new IntakeArmToggleCommand(this.m_robotMap.getIntake()).andThen(() -> System.out.println("finished turn around")),
            new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), m_reverseAfterFinished, m_motorSpeed).andThen(() -> System.out.println("finished back away"))
        );
    } 

    private Command getPathPlanningAutonomousCommand() {
        Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve("paths/Go back.wpilib.json");
        Trajectory trajectory;
        try {
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        this.m_robotMap.getDriveTrain().resetOdometry(trajectory.getInitialPose());

        return new RamseteCommand(
            trajectory,
            DriveTrainSubsystem::getPose,
            new RamseteController(Constants.RAMSETE_B, Constants.RAMSETE_Zeta),
            new SimpleMotorFeedforward(Constants.KS_VOLTS, Constants.KV_VOLTS, Constants.KA_VOLTS),
            this.m_robotMap.getDriveTrain().getKinematics(),
            DriveTrainSubsystem::getWheelSpeeds,
            new PIDController(Constants.KP_DRIVE_VEL, 0, 0),
            new PIDController(Constants.KP_DRIVE_VEL, 0, 0),
            DriveTrainSubsystem::tankDriveVolts,
            this.m_robotMap.getDriveTrain()
        ).andThen(() -> this.m_robotMap.getDriveTrain().stopMotors());
    }

    public void cancelCommands() {
        if (this.m_currentCommands != null) {
            this.m_currentCommands.cancel();
        }
    }
}
