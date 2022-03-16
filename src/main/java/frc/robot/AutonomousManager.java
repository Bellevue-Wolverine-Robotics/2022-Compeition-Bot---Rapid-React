package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ArcadeDriveDistanceCommand;
import frc.robot.commands.ArcadeDriveTurnCommand;
import frc.robot.commands.ArcadeDriveUntilCloseCommand;
import frc.robot.commands.IntakeArmToggleCommand;
import frc.robot.commands.IntakeReverseCommand;
import frc.robot.commands.IntakeStartCommand;
import frc.robot.commands.IntakeStopCommand;

public class AutonomousManager {
    private RobotMap m_robotMap;

    private DigitalInput m_switch1 = new DigitalInput(0);

    private CommandGroupBase m_currentCommands;

    public AutonomousManager(RobotMap robotMap) {
        this.m_robotMap = robotMap;
    }

    
    // Please read about command groups (https://docs.wpilib.org/en/stable/docs/software/commandbased/command-groups.html)
    // to understand how the plans are being made
    public void autonomousInit() {
        
        // Define variables up here so we can change them easily
        // Motor speed drop will be controlled in IntakeSubsystem
        
        double motorSpeed = SmartDashboard.getNumber("motorSpeed", 0.3);                                                          // This is in percent
        double intakeReverseTimeToScore = SmartDashboard.getNumber("intakeReverseTimeToScore", 2);                                // This is in seconds
        double distanceToDriveAfterIntakeMotorSpeedDrop = SmartDashboard.getNumber("distanceToDriveAfterIntakeMotorSpeedDrop", 2);// This is in inches
        double turnAroundAngle = SmartDashboard.getNumber("turnAroundAngle", 171);                                                // This is in degrees
        double distanceFromHubToScore = SmartDashboard.getNumber("distanceFromHubToScore", 12);                                   // This is in inches
        double reverseAfterFinished = SmartDashboard.getNumber("reverseAfterFinished", 141);                                      // This is in inches
        double turnAroundAfterScoreAngle = SmartDashboard.getNumber("turnAroundAfterScoreAngle", 180);                            // This is in degrees

        // This if statement can be change (actually please change it) once we know
        // more about how we're going to control the different auto plans
        if (this.m_switch1.get()) {

            // 1 ball auto
            SequentialCommandGroup oneBallAuto = new SequentialCommandGroup(
                // Since we start just outside of lower hopper, just spit out ball
                // The parallel command group will just ensure the intake is run for the right amount of time
                new ParallelCommandGroup(
                    new IntakeReverseCommand(this.m_robotMap.getIntake()),
                    new WaitCommand(intakeReverseTimeToScore)    
                ).andThen(() -> System.out.println("finished scoring")),

                // Stop the intake
                new IntakeStopCommand(this.m_robotMap.getIntake()).andThen(() -> System.out.println("finished stopped intake")),
                                
                // flip a 180, lower the hopper and drive away
                new ParallelCommandGroup(
                    new ArcadeDriveTurnCommand(this.m_robotMap, turnAroundAfterScoreAngle, motorSpeed),
                    new IntakeArmToggleCommand(this.m_robotMap.getIntake())                    
                ).andThen(() -> System.out.println("finished turn around")),
                new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), reverseAfterFinished, motorSpeed).andThen(() -> System.out.println("finished back away"))
            );
            this.m_currentCommands = oneBallAuto;

        } else {

            // 2 ball auto
            SequentialCommandGroup twoBallAuto = new SequentialCommandGroup(
                // turn on intake 
                new IntakeStartCommand(this.m_robotMap.getIntake()),

                // Move hopper down and move forward, until our motor speed drops
                // Parallel command group will run all these commands at the same time
                new ParallelCommandGroup(
                    new IntakeArmToggleCommand(this.m_robotMap.getIntake()),

                    // This command *should* get interrupted, but it will stop after 5 feet for safety
                    new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 60, motorSpeed)

                ).withInterrupt(() -> {
                    // This withInterrupt() call will stop the parallel command group when
                    // this lambda returns true

                    // Stop the command once we've intaked the ball
                    return this.m_robotMap.getIntake().hasIntakedBall();
                }).andThen(() -> System.out.println("finished intake ball")),

                // Drive 2 more inches then turn around 170 degrees
                new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), distanceToDriveAfterIntakeMotorSpeedDrop, motorSpeed).andThen(() -> System.out.println("finished drive 2 more inches")),
                new ArcadeDriveTurnCommand(this.m_robotMap, turnAroundAngle, motorSpeed).andThen(() -> System.out.println("finished turn")),

                // Raise intake and stop it
                new IntakeStopCommand(this.m_robotMap.getIntake()),
                new IntakeArmToggleCommand(this.m_robotMap.getIntake()).andThen(() -> System.out.println("finished stop and toggle intake")),

                // Drive to up until within XX inches
                new ArcadeDriveUntilCloseCommand(this.m_robotMap, distanceFromHubToScore, motorSpeed).andThen(() -> System.out.println("finished drive until close")),
                
                // Reverse intake motors and stop them
                new ParallelCommandGroup(
                    new IntakeReverseCommand(this.m_robotMap.getIntake()),
                    new WaitCommand(intakeReverseTimeToScore)                    
                ).andThen(() -> System.out.println("finished scoring")),
                new IntakeStopCommand(this.m_robotMap.getIntake()).andThen(() -> System.out.println("finished stop intake")),

                // flip a 180, lower the hopper and drive away
                new ParallelCommandGroup(
                    new ArcadeDriveTurnCommand(this.m_robotMap, turnAroundAfterScoreAngle, motorSpeed),
                    new IntakeArmToggleCommand(this.m_robotMap.getIntake())                    
                ).andThen(() -> System.out.println("finished turn around")),
                new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), reverseAfterFinished, motorSpeed).andThen(() -> System.out.println("finished back away"))
            );
            this.m_currentCommands = twoBallAuto;

        }

        // Start plan along with calibration
        this.m_currentCommands.schedule();
    }

    public void autonomousPeriodic() {

    }

    public void cancelCommands() {
        if (this.m_currentCommands != null) {
            this.m_currentCommands.cancel();
        }
    }
}
