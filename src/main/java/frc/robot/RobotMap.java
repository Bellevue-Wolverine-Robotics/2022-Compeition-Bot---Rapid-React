package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Commands.ActivateIntakeCommand;
import frc.robot.Commands.JoystickArcadeDriveCommand;
import frc.robot.Subsystems.*;

/* Creates robot subsystems and commands, binds those commands to triggering 
    events (such as buttons), and specify which commands will run in autonomous. */
public class RobotMap {
    private final Joystick m_joystick1 = new Joystick(Constants.JOYSTICK_1);
	private final Joystick m_joystick2 = new Joystick(Constants.JOYSTICK_2);
	private final Joystick m_joystick3 = new Joystick(Constants.JOYSTICK_3);

    //private final ADXRS450_Gyro m_gyro = new ADXRS450_Gyro();

	private final DriveTrainSubsystem m_driveTrain = new DriveTrainSubsystem();

    private final ColorSensorSubsystem m_colorSensor = new ColorSensorSubsystem();

    private final IntakeSubsystem m_intake = new IntakeSubsystem();

    public RobotMap() {
		this.configureDefaultCommands();
		this.configureButtonBindings();
	}

    private void configureButtonBindings() {
        JoystickButton test = new JoystickButton(m_joystick2, Constants.JOYSTICK_2_INTAKE_BUTTON);
        test.whenPressed(new ActivateIntakeCommand(m_intake));
    }

    private void configureDefaultCommands() {
        JoystickArcadeDriveCommand command = new JoystickArcadeDriveCommand(this.m_driveTrain, this.m_joystick1);
        /* Test of command based robot control */
        this.m_driveTrain.setDefaultCommand(command);
    }

    public void onDisable() {
        //this.m_gyro.calibrate();
	}

    public DriveTrainSubsystem getDriveTrain() {
        return this.m_driveTrain;
    }

    // public ADXRS450_Gyro getGyro() {
    //     return this.m_gyro;
    // }

    public ColorSensorSubsystem getColorSensor() {
        return this.m_colorSensor;
    }
}
