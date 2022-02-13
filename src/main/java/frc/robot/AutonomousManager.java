package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ArcadeDriveDistanceCommand;
import frc.robot.commands.IntakeReverseCommand;

public class AutonomousManager {
    private RobotMap m_robotMap;

    private DigitalInput m_switch1 = new DigitalInput(0);

    private boolean m_areWeDoingOneBallAuto = true;

    private CommandGroupBase m_currentCommands;

    public AutonomousManager(RobotMap robotMap) {
        this.m_robotMap = robotMap;
    }

    public void autonomousInit() {
        // Init commands/auto here
        
        // Please read about command groups (https://docs.wpilib.org/en/stable/docs/software/commandbased/command-groups.html)
        // to understand how the plans are being made

        // This if statement can be change (actually please change it) once we know
        // more about how we're going to control the different auto plans
        if (this.m_areWeDoingOneBallAuto) {
            // 1 ball auto
            SequentialCommandGroup oneBallAuto = new SequentialCommandGroup(
                // Since we start just outside of lower hopper, just spit out ball
                // withTimeout simply specifies how long the command should run (because it runs forever by default)
                new IntakeReverseCommand(this.m_robotMap.getIntake()).withTimeout(5),
                
                // Drive backwards a bunch to get off tarmac
                new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 72, -0.5)
            );
            this.m_currentCommands = oneBallAuto;
        } else {
            // 2 ball auto
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
