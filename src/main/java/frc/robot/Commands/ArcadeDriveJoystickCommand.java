package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrainSubsystem;

public class ArcadeDriveJoystickCommand extends CommandBase {

    private final DriveTrainSubsystem m_driveTrainSubsystem;
    private final Joystick m_joystick1;

    public ArcadeDriveJoystickCommand(DriveTrainSubsystem driveTrainSubsystem, Joystick Joystick1) {
        this.m_driveTrainSubsystem = driveTrainSubsystem;
        this.m_joystick1 = Joystick1;

        // This is to make the command require the subsystem
        addRequirements(this.m_driveTrainSubsystem);
    }

    @Override
    public void execute() {
        // Forward is negative on the joy stick, which is a bit weird
        double forwardBack = -this.m_joystick1.getY();

        // This doesn't hold true on the other joystick axis. 
        // Confusing I know. Left is negative, right is positive on the axis
        double leftRight = this.m_joystick1.getX();

        new ArcadeDriveCommand(this.m_driveTrainSubsystem, forwardBack, leftRight).execute();
    }
} 
