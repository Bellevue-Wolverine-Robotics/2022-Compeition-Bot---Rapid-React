package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrainSubsystem;

public class ArcadeDriveDistanceCommand extends CommandBase {

    private final DriveTrainSubsystem m_driveTrainSubsystem;

    private final double m_forwardBackInchesDistance;
    private double m_startingPosition;

    private double m_speed;

    private boolean m_isFinished = false;

    public ArcadeDriveDistanceCommand(DriveTrainSubsystem driveTrainSubsystem, double forwardBackInchesDistance, double speed) {
        this.m_driveTrainSubsystem = driveTrainSubsystem;

        this.m_forwardBackInchesDistance = forwardBackInchesDistance;

        if (speed > 1 || speed < -1) {
            throw new IllegalArgumentException("Speed at invalid value: " + speed);
        }
        this.m_speed = speed;
        
        // This is to make the command require the subsystem
        addRequirements(this.m_driveTrainSubsystem);
    }

    @Override
    public void initialize() {
        this.m_startingPosition = this.m_driveTrainSubsystem.getLeftBackMotor().getEncoder().getPosition();
        this.m_isFinished = false;
    }

    @Override
    public void execute() {
        // Check if distance travelled from start is larger than target position
        double currentPosition = this.m_driveTrainSubsystem.getLeftBackMotor().getEncoder().getPosition();
        if (Math.abs(currentPosition - this.m_startingPosition) > this.m_forwardBackInchesDistance) {
            // If it is then the command is finished and we should return
            this.m_isFinished = true;
            this.m_driveTrainSubsystem.stopMotors();
            return;
        }

        // If not keep moving at the given speed
        this.m_driveTrainSubsystem.arcadeDrive(this.m_speed, 0);
    }

    @Override
    public boolean isFinished() {
        return this.m_isFinished;
    }
} 
