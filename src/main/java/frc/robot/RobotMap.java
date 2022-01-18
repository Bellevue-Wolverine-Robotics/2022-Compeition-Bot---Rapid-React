package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Commands.JoystickArcadeDriveCommand;
import frc.robot.Subsystems.*;

public class RobotMap {
    private final Joystick Joystick1 = new Joystick(Constants.JOYSTICK_1);
	private final Joystick Joystick2 = new Joystick(Constants.JOYSTICK_2);
	private final Joystick Joystick3 = new Joystick(Constants.JOYSTICK_3);

	private final DriveTrainSubsystem m_driveTrain = new DriveTrainSubsystem();

    public RobotMap() {
		this.configureDefaultCommands();
		this.configureButtonBindings();
	}

    private void configureButtonBindings() {
    }

    private void configureDefaultCommands() {
        JoystickArcadeDriveCommand command = new JoystickArcadeDriveCommand(this.m_driveTrain, Joystick1);
        /* Test of command based robot control */
        this.m_driveTrain.setDefaultCommand(command);
    }

    public void onDisable() {

	}

    public DriveTrainSubsystem getDriveTrain() {
        return this.m_driveTrain;
    }
}
