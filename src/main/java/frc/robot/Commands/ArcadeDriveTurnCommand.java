package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotMap;

public class ArcadeDriveTurnCommand extends CommandBase {

    private final RobotMap m_robotMap;

    private final double m_leftRightDegrees;
    private final ArcadeDriveCommand m_moveCommand;
    private final double m_startingAngle;

    private boolean m_isFinished = false;

    public ArcadeDriveTurnCommand(RobotMap robotMap, double turnDegrees, double speed) {
        this.m_robotMap = robotMap;

        this.m_leftRightDegrees = turnDegrees;

        if (speed > 1 || speed < -1) {
            throw new IllegalArgumentException("Speed at invalid value: " + speed);
        }
        
        this.m_moveCommand = new ArcadeDriveCommand(this.m_robotMap.getDriveTrain(), 0, speed);
        this.m_startingAngle = this.m_robotMap.getGyro().getAngle();
        
        // This is to make the command require the subsystem
        addRequirements(this.m_robotMap.getDriveTrain());
    }

    @Override
    public void execute() {
        // Check if distance turned from start is larger than target total turn amount
        double currentPosition = this.m_robotMap.getGyro().getAngle();
        if (Math.abs(currentPosition - this.m_startingAngle) > this.m_leftRightDegrees) {
            // If it is then the command is finished and we should return
            this.m_isFinished = true;
            return;
        }

        // If not keep moving at the given speed
        this.m_moveCommand.execute();
    }

    @Override
    public boolean isFinished() {
        return this.m_isFinished;
    }
} 