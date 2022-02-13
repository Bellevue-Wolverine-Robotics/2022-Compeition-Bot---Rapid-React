package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Commands.ArcadeDriveDistanceCommand;
import frc.robot.Commands.IntakeReverseCommand;

public class AutonomousManager {
    private RobotMap m_robotMap;

    private DigitalInput m_switch1 = new DigitalInput(0);

    private Command m_currentCommand;
    private int m_step = 1;

    private Command m_step1Command;
    private Command m_step2Command;
    private Command m_step3Command;

    public AutonomousManager(RobotMap robotMap) {
        this.m_robotMap = robotMap;
    }

    public void autonomousInit() {
        // Init commands/auto here
        
        // Move forward till it's close 
        // This ArcadeDriveDistanceCommand is just here for debugging purposes, 
        // it will be replaced with a command the makes the robot move until close to something later
        this.m_step1Command = new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 72, 0.5);
        
        // Spit out ball from hopper system to score // withTimeout simply specifies how long the command should run (because it runs forever by default)
        this.m_step2Command = new IntakeReverseCommand(this.m_robotMap.getIntake()).withTimeout(5);
        
        // Drive backwards a bunch to get off tarmac
        this.m_step3Command = new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 72, -0.5);

        // Actually start the autonomous
        this.m_currentCommand = this.m_step1Command;
        this.m_currentCommand.schedule();
        this.m_step = 1;
    }

    public void autonomousPeriodic() {
        switch (this.m_step) {
            case 1:
                waitForNextStep(m_step2Command);
                break;
            case 2: 
                waitForNextStep(m_step3Command);
                break;
            default:
                //System.out.println("Unknown Autonomous Step");
                break;
        }
    }

    private void waitForNextStep(Command nextCommand) {
        // check if the current command isFinished, if it is, then move onto next step
        if (this.m_currentCommand.isFinished() || !this.m_currentCommand.isScheduled()) {
            this.m_step++;

            this.m_currentCommand = nextCommand;
            this.m_currentCommand.schedule();
            System.out.println("Moving to auto step: " + this.m_step);
        }
    }

    public void cancelCommands() {
        if (this.m_currentCommand != null) {
            this.m_currentCommand.cancel();
        }
        this.m_step = 0;
    }
}
