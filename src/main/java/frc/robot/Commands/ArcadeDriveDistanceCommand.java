package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.DriveTrainSubsystem;

public class ArcadeDriveDistanceCommand extends CommandBase {

    private final DriveTrainSubsystem m_driveTrainSubsystem;

    private final double m_forwardBackInchesDistance;
    private final ArcadeDriveCommand m_moveCommand;
    private double m_startingPosition;

    private boolean m_isFinished = false;

    public ArcadeDriveDistanceCommand(DriveTrainSubsystem driveTrainSubsystem, double forwardBackInchesDistance, double speed) {
        this.m_driveTrainSubsystem = driveTrainSubsystem;

        this.m_forwardBackInchesDistance = forwardBackInchesDistance;

        if (speed > 1 || speed < -1) {
            throw new IllegalArgumentException("Speed at invalid value: " + speed);
        }
        
        this.m_moveCommand = new ArcadeDriveCommand(this.m_driveTrainSubsystem, speed, 0);
        
        // This is to make the command require the subsystem
        addRequirements(this.m_driveTrainSubsystem);
    }

    @Override
    public void initialize() {
        this.m_startingPosition = this.m_driveTrainSubsystem.getLeftBackMotor().getEncoder().getPosition();
    }

    @Override
    public void execute() {
        // Check if distance travelled from start is larger than target position
        double currentPosition = this.m_driveTrainSubsystem.getLeftBackMotor().getEncoder().getPosition();
        if (Math.abs(currentPosition - this.m_startingPosition) > this.m_forwardBackInchesDistance) {
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
