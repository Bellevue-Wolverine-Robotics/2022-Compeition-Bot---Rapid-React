package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/* Creates robot subsystems and commands, binds those commands to triggering 
    events (such as buttons), and specify which commands will run in autonomous. */
public class RobotMap {
    private final Joystick m_joystick1 = new Joystick(Constants.JOYSTICK_1);
	private final Joystick m_joystick2 = new Joystick(Constants.JOYSTICK_2);
	private final Joystick m_joystick3 = new Joystick(Constants.JOYSTICK_3);

    // yes the class name is weird, the docs say it's a misnomer
    // Also put this at least a ft away from the front bumper, also leave it 1-2ft above the ground
    private final AnalogPotentiometer m_ultrasonicSensor = new AnalogPotentiometer(0, Constants.INCHES_PER_5V);

    private final ADXRS450_Gyro m_gyro = new ADXRS450_Gyro();

	private final DriveTrainSubsystem m_driveTrain = new DriveTrainSubsystem();

    private final ClimbSubsystem m_climb = new ClimbSubsystem();

    private final IntakeSubsystem m_intake = new IntakeSubsystem();

    private final ColorSensorSubsystem m_colorSensor = new ColorSensorSubsystem();
    
    public RobotMap() {
		this.configureDefaultCommands();
		this.configureButtonBindings();
	}

    private void configureButtonBindings() {
        // Notice the difference between assignActionToButtonPress and assignActionToButtonHold

        // Climb
        Joystick climbJoystick = this.m_joystick3; // The joystick variables will make it easier to swap joysticks
        assignActionToButtonHold(new ClimbArmExtendCommand(this.m_climb), Constants.LONG_ARM_EXTEND_BUTTON, climbJoystick);
        assignActionToButtonHold(new ClimbArmRetractCommand(this.m_climb), Constants.LONG_ARM_RETRACT_BUTTON, climbJoystick);
        assignActionToButtonHold(new ClimbArmPivotCommand(this.m_climb), Constants.LONG_ARM_PIVOT_BUTTON, climbJoystick);
        assignActionToButtonHold(new ClimbArmPivotReverseCommand(this.m_climb), Constants.LONG_ARM_PIVOT_REVERSE_BUTTON, climbJoystick);
        assignActionToButtonPress(new ClimbToggleHooksCommand(this.m_climb), Constants.HOOKS_TOGGLE_BUTTON, climbJoystick);

        // Intake
        Joystick intakeJoystick = this.m_joystick3;
        assignActionToButtonHold(new IntakeStartCommand(this.m_intake), Constants.INTAKE_START_BUTTON, intakeJoystick);
        assignActionToButtonHold(new IntakeReverseCommand(this.m_intake), Constants.INTAKE_REVERSE_BUTTON, intakeJoystick);
        assignActionToButtonPress(new IntakeArmToggleCommand(this.m_intake), Constants.INTAKE_TOGGLE_BUTTON, intakeJoystick);
    }

    private JoystickButton assignActionToButtonPress(Command command, int buttonId, Joystick joystick) {
        JoystickButton button = new JoystickButton(joystick, buttonId);
        button.whenPressed(command);
        return button;
    }

    private JoystickButton assignActionToButtonHold(Command command, int buttonId, Joystick joystick) {
        JoystickButton button = new JoystickButton(joystick, buttonId);
        button.whenHeld(command);
        return button;
    }

    private void configureDefaultCommands() {
        ArcadeDriveJoystickCommand command = new ArcadeDriveJoystickCommand(this.m_driveTrain, this.m_joystick1);
        /* Test of command based robot control */
        this.m_driveTrain.setDefaultCommand(command);
    }

    public void onDisable() {
        this.m_gyro.calibrate();
	}

    public DriveTrainSubsystem getDriveTrain() {
        return this.m_driveTrain;
    }

    public ClimbSubsystem getClimb() {
        return this.m_climb;
    }
    
    public IntakeSubsystem getIntake() {
        return this.m_intake;
    }

    public ADXRS450_Gyro getGyro() {
        return this.m_gyro;
    }

    public ColorSensorSubsystem getColorSensor() {
        return this.m_colorSensor;
    }

    public AnalogPotentiometer getUltrasonicSensor() {
        return this.m_ultrasonicSensor;
    }
}
