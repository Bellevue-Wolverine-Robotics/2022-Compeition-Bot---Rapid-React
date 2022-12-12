package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotMap;

public class ArcadeDriveUntilCloseCommand extends CommandBase {

    private final RobotMap m_robotMap;

    private final double m_threshold;
    private final double m_speed;

    private boolean m_isFinished = false;

    public ArcadeDriveUntilCloseCommand(RobotMap robotMap, double thresholdInInches, double speed) {
        this.m_robotMap = robotMap;

        this.m_threshold = thresholdInInches;
        this.m_speed = speed;

        // This is to make the command require the subsystem
        addRequirements(this.m_robotMap.getDriveTrain());
    }

    @Override
    public void initialize() {
        this.m_isFinished = false;
    }

    @Override
    public void execute() {
        if (this.m_robotMap.getUltrasonicSensor().get() < this.m_threshold) {
            this.m_isFinished = true;
            this.m_robotMap.getDriveTrain().stopMotors();
        }
        this.m_robotMap.getDriveTrain().arcadeDrive(this.m_speed, 0);
    }

    @Override
    public boolean isFinished() {
        return this.m_isFinished;
    }
} 
