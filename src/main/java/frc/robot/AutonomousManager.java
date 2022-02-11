package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Commands.ArcadeDriveDistanceCommand;
import frc.robot.Commands.IntakeReverseCommand;

public class AutonomousManager {
    private RobotMap m_robotMap;

    private DigitalInput m_switch1 = new DigitalInput(0);

    private Command m_currentCommand;
    private boolean m_isRunningCommand = false;
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
        //this.m_step1Command = new ArcadeDriveUntilClose(this.m_robotMap.getDriveTrain(), 18, 0.3);
        
        // Spit out ball from hopper system to score // withTimeout simply specifies how long the command should run (because it runs forever by default)
        this.m_step2Command = new IntakeReverseCommand(this.m_robotMap.getIntake()).withTimeout(1);
        
        // Drive backwards a bunch to get off tarmac
        this.m_step3Command = new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 72, -1);
    }

    public void autonomousPeriodic() {
        switch (this.m_step) {
            case 1:
                doStep(m_step1Command);
                break;
            case 2: 
                doStep(m_step2Command);
                break;
            case 3:
                doStep(m_step3Command);
                break;
            default:
                System.out.println("Unknown Autonomous Step");
                break;
        }
    }

    private void doStep(Command nextCommand) {
        // check if the command is not running, if not then schedule it
        if (!this.m_isRunningCommand) {
            System.out.println("Starting auto step: " + this.m_step);
            this.m_currentCommand = nextCommand;
            this.m_currentCommand.schedule();
            
            this.m_isRunningCommand = true;
        } else if (this.m_currentCommand.isFinished()) {
            // If it finished then move on
            this.m_step++;
            this.m_isRunningCommand = false;

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
