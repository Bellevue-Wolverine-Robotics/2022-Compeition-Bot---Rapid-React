package frc.robot;

public class Constants {
    /*
     * Joysticks
     */
	public static final int JOYSTICK_1 = 0;
	public static final int JOYSTICK_2 = 1;
	public static final int JOYSTICK_3 = 2;
    
    /*
     * Drive
     */
    // CAN IDs
    public static final int LEFT_FRONT = 1;
    public static final int LEFT_BACK = 2;
    public static final int RIGHT_FRONT = 3;
    public static final int RIGHT_BACK = 4;

    // Inputs
	public static final int QUICKTURN_BUTTON = 1;

    // Calculations        
    public static final int WHEEL_SIZE = 6;
    public static final double GEAR_RATIO = 1 / 10.71;
    // Calculate the position factor by calculating the circumference of the wheel times the gear ratio
    // From https://dev.revrobotics.com/sparkmax/software-resources/migrating-ctre-to-rev#change-units-from-rotations-to-inches
    public static final double POSITION_FACTOR = Math.PI * WHEEL_SIZE * GEAR_RATIO;

    
    // All ID's beyond this point are assumptions, except the test motors.
    /*
     * Shooter
     */
    // CAN IDs
    public static final int SHOOTER_MOTOR = 8;

    // Motor or piston now dunno which
    public static final int HEAD_CONTROL_MOTOR = 9;
    public static final int HEAD_CONTROL_PISTON_DEPLOY = 6;
    public static final int HEAD_CONTROL_PISTON_RETRACT = 7;

    // Input
    public static final int FLYWHEEL_BUTTON = 1;


    /*
     * Climb
     */
    // CAN IDs
    public static final int LONG_ARM_EXTEND_MOTOR = 6;
    public static final int LONG_ARM_PIVOT_MOTOR = 7;

    // PCM IDs
    public static final int HOOKS_DEPLOY = 4;
    public static final int HOOKS_RETRACT = 5;

    // Input
    public static final int LONG_ARM_PIVOT_BUTTON = 5;
    public static final int LONG_ARM_PIVOT_REVERSE_BUTTON = 3;
    public static final int LONG_ARM_EXTEND_BUTTON = 6;
    public static final int LONG_ARM_RETRACT_BUTTON = 4;
    public static final int HOOKS_TOGGLE_BUTTON = 2;


    /*
     * Intake
     */
    // CAN IDs
    public static final int INTAKE_MOTOR = 5;

    // PCM IDs
    public static final int INTAKE_LEFT_DEPLOY = 0;
    public static final int INTAKE_LEFT_RETRACT = 1;
    public static final int INTAKE_RIGHT_DEPLOY = 2;
    public static final int INTAKE_RIGHT_RETRACT = 3;

    // Input
	public static final int INTAKE_START_BUTTON = 11;
	public static final int INTAKE_REVERSE_BUTTON = 12;
	public static final int INTAKE_TOGGLE_BUTTON = 9;


    /*
     * Test motors
     */
    // public static final int NEO_ID = 5;
    // public static final int REDLINE_ID = 6; // Test intake
}
