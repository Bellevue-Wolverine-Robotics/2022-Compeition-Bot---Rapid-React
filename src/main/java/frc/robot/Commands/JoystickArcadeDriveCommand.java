package frc.robot.Commands;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.DriveTrainSubsystem;

public class JoystickArcadeDriveCommand extends CommandBase {

    private final DriveTrainSubsystem m_driveTrainSubsystem;
    private final Joystick m_joystick1;

    public JoystickArcadeDriveCommand(DriveTrainSubsystem driveTrainSubsystem, Joystick Joystick1) {
        this.m_driveTrainSubsystem = driveTrainSubsystem;
        this.m_joystick1 = Joystick1;

        // This is to make the command require the subsystem
        addRequirements(driveTrainSubsystem);
    }

    @Override
    public void execute() {
        // Forward is negative on the joy stick, which is a bit weird
        double forwardBack = -this.m_joystick1.getY();
        double leftRight = -this.m_joystick1.getX();

        new ArcadeDriveCommand(this.m_driveTrainSubsystem, forwardBack, leftRight).execute();
    }
} 
