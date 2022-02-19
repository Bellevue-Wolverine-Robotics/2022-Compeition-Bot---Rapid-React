package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ArcadeDriveDistanceCommand;
import frc.robot.commands.ArcadeDriveTurnCommand;
import frc.robot.commands.IntakeArmToggleCommand;
import frc.robot.commands.IntakeReverseCommand;
import frc.robot.commands.IntakeStartCommand;
import frc.robot.commands.IntakeStopCommand;

public class AutonomousManager {
    private RobotMap m_robotMap;

    private DigitalInput m_switch1 = new DigitalInput(0);
    private boolean m_areWeDoingOneBallAuto = true;

    private CommandGroupBase m_currentCommands;

    public AutonomousManager(RobotMap robotMap) {
        this.m_robotMap = robotMap;
    }

    
    // Please read about command groups (https://docs.wpilib.org/en/stable/docs/software/commandbased/command-groups.html)
    // to understand how the plans are being made
    public void autonomousInit() {
        
        // Define variables up here so we can change them easily
        // Motor speed drop will be controlled in IntakeSubsystem
        
        double motorSpeed = 0.7;                                     // This is in percent
        double intakeReverseTimeToScore = 2;                    // This is in seconds
        double distanceToDriveAfterIntakeMotorSpeedDrop = 2;    // This is in inches
        double turnAroundAngle = 171;                           // This is in degrees
        double distanceFromHubToScore = 12;                    // This is in inches
        double reverseAfterFinished = 114;                      // This is in inches


        // This if statement can be change (actually please change it) once we know
        // more about how we're going to control the different auto plans
        if (this.m_areWeDoingOneBallAuto) {

            // 1 ball auto
            SequentialCommandGroup oneBallAuto = new SequentialCommandGroup(
                // Since we start just outside of lower hopper, just spit out ball
                // withTimeout simply specifies how long the command should run (because it runs forever by default)
                new IntakeReverseCommand(this.m_robotMap.getIntake()).withTimeout(intakeReverseTimeToScore),

                // Stop the intake
                new IntakeStopCommand(this.m_robotMap.getIntake()),
                
                // Drive backwards a bunch to get off tarmac
                new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 72, -0.5)
            );
            this.m_currentCommands = oneBallAuto;

        } else {

            // 2 ball auto
            SequentialCommandGroup twoBallAuto = new SequentialCommandGroup(
                // Move hopper down, turn on intake and move forward, until our motor speed drops
                // Parallel command group will run all these commands at the same time
                new ParallelCommandGroup(
                    new IntakeArmToggleCommand(this.m_robotMap.getIntake()),
                    new IntakeStartCommand(this.m_robotMap.getIntake()),

                    // This command *should* get interrupted, but it will stop after 5 feet for safety
                    new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 60, motorSpeed)

                ).withInterrupt(() -> {
                    // This withInterrupt() call will stop the parallel command group when
                    // this lambda returns true

                    // Stop the command once we've intaked the ball
                    return this.m_robotMap.getIntake().hasIntakedBall();
                }),

                // Drive 2 more inches then turn around 170 degrees
                new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), distanceToDriveAfterIntakeMotorSpeedDrop, motorSpeed),
                new ArcadeDriveTurnCommand(this.m_robotMap, turnAroundAngle, motorSpeed),

                // Raise intake and stop it
                new ParallelCommandGroup(
                    new IntakeArmToggleCommand(this.m_robotMap.getIntake()),
                    new IntakeStopCommand(this.m_robotMap.getIntake())
                ),

                // Drive to up until within XX inches
                new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), distanceFromHubToScore, motorSpeed),
                
                // Reverse intake motors and stop them
                new IntakeReverseCommand(this.m_robotMap.getIntake()).withTimeout(intakeReverseTimeToScore),
                new IntakeStopCommand(this.m_robotMap.getIntake()),

                // Reverse away
                new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), reverseAfterFinished, motorSpeed)
            );
            this.m_currentCommands = twoBallAuto;

        }

        // Start plan
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
