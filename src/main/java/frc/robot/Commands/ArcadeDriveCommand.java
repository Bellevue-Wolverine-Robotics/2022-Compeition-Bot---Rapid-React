package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Subsystems.DriveTrainSubsystem;

// Inheriting from instantcommand will have it execute once then finished
public class ArcadeDriveCommand extends InstantCommand {

    private final DriveTrainSubsystem m_driveTrainSubsystem;

    private final double m_forwardBack;
    private final double m_leftRight;

    public ArcadeDriveCommand(DriveTrainSubsystem driveTrainSubsystem, double forwardBack, double leftRight) {
        this.m_driveTrainSubsystem = driveTrainSubsystem;

        this.m_forwardBack = forwardBack;
        this.m_leftRight = leftRight;

        // This is to make the command require the subsystem
        addRequirements(this.m_driveTrainSubsystem);
    }

    @Override
    public void execute() {
        this.m_driveTrainSubsystem.arcadeDrive(this.m_forwardBack, this.m_leftRight);
    }
} 
