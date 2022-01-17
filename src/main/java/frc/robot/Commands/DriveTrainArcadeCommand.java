/*package frc.robot.Commands;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.DriveTrainSubsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrainArcadeCommand extends CommandBase {

    private final DifferentialDrive driveTrainSubsystem;
    private final Joystick Joystick1;

    public DriveTrainArcadeCommand(DifferentialDrive driveTrainSubsystem, Joystick Joystick1) {
        this.driveTrainSubsystem = driveTrainSubsystem;
        this.Joystick1 = Joystick1;
    }

    @Override
    public void execute() {
        double forwardBack = this.Joystick1.getY();
        double leftRight = this.Joystick1.getX();

        this.driveTrainSubsystem.arcadeDrive(forwardBack, leftRight);
    }

    
} 
*/