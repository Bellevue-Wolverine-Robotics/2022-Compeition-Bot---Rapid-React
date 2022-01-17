package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Commands.DriveTrainArcadeCommand;
import frc.robot.Subsystems.*;

public class RobotMap {
    private final Joystick Joystick1 = new Joystick(0);
	private final Joystick Joystick2 = new Joystick(1);
	private final Joystick Joystick3 = new Joystick(2);

	private final DriveTrainSubsystem driveTrain = new DriveTrainSubsystem();

    public RobotMap() {
		this.configureDefaultCommands();
		this.configureButtonBindings();
	}

    private void configureButtonBindings() {
    }

    private void configureDefaultCommands() {
        /* Test of command based robot control */
        this.driveTrain.setDefaultCommand(new DriveTrainArcadeCommand(driveTrain, Joystick1));

    }
		


    public void onDisable() {

	}
    

}
