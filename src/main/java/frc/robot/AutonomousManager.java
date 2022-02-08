package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Commands.ArcadeDriveDistanceCommand;

public class AutonomousManager {
    private RobotMap m_robotMap;

    private DigitalInput m_switch1 = new DigitalInput(0);

    private Command m_currentCommand;
    private boolean m_isRunningCommand = false;
    private int m_step = 1;

    private Command step1Command;

    public AutonomousManager(RobotMap robotMap) {
        this.m_robotMap = robotMap;
    }

    public void autonomousInit() {
        this.step1Command = new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 18, 0.3);
    }

    public void autonomousPeriodic() {
        switch (this.m_step) {
            case 1:
                doStep(step1Command);
                break;
            case 2: 
                if (m_switch1.get()) {
                    System.out.println("Switch is on");
                } else {
                    System.out.println("Switch is off");
                }
                break;
            default:
                System.out.println("Unknown Autonomous Step");
                break;
        }
  
        // if (this.m_autonomousCommand != null) {
        // 	this.m_autonomousCommand.schedule();
        // }
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
        this.m_currentCommand.cancel();
        this.m_step = 0;
    }
}
