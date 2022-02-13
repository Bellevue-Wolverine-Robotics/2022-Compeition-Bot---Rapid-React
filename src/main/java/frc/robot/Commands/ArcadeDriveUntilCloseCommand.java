package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotMap;

public class ArcadeDriveUntilCloseCommand extends CommandBase {

    private final RobotMap m_robotMap;

    private final double m_threshold;
    private final double m_speed;

    public ArcadeDriveUntilCloseCommand(RobotMap robotMap, double speed, double thresholdInMM) {
        this.m_robotMap = robotMap;

        this.m_speed = speed;
        this.m_threshold = thresholdInMM;

        // This is to make the command require the subsystem
        addRequirements(this.m_robotMap.getDriveTrain());
    }

    @Override
    public void execute() {
        new ArcadeDriveCommand(this.m_robotMap.getDriveTrain(), this.m_speed, 0).execute();
    }

    @Override
    public boolean isFinished() {
        return this.m_robotMap.getUltrasonicSensor().get() < this.m_threshold;
    }
} 
