package frc.robot;

public class Constants {
    /*
     * Input
     */
	public static final int JOYSTICK_1 = 0;
	public static final int JOYSTICK_2 = 1;
	public static final int JOYSTICK_3 = 2;

	public static final int QUICKTURN_BUTTON = 1;
	public static final int JOYSTICK_2_INTAKE_BUTTON = 1;

    
    /*
     * Drive
     */
    public static final int LEFT_FRONT = 1;
    public static final int LEFT_BACK = 2;
    public static final int RIGHT_FRONT = 3;
    public static final int RIGHT_BACK = 4;

        
    public static final int WHEEL_SIZE = 6;
    public static final double GEAR_RATIO = 1 / 10.71;
    // Calculate the position factor by calculating the circumference of the wheel times the gear ratio
    // From https://dev.revrobotics.com/sparkmax/software-resources/migrating-ctre-to-rev#change-units-from-rotations-to-inches
    public static final double POSITION_FACTOR = Math.PI * WHEEL_SIZE * GEAR_RATIO;

    
    /*
     * Shooter
     */
    public static final int SHOOTER_MOTOR = -1;
    // Motor or piston now dunno which
    public static final int HEAD_CONTROL_MOTOR = -1;
    public static final int HEAD_CONTROL_PISTON = -1;

    /*
     * Climb
     */
    public static final int LONG_ARM_MOTOR_1 = -1;
    public static final int LONG_ARM_MOTOR_2 = -1;

    public static final int SMALL_ARM_DEPLOY = -1;
    public static final int SMALL_ARM_RETRACT = -1;


    /*
     * Intake
     */
    public static final int INTAKE_MOTOR = -1;

    public static final int INTAKE_LEFT_DEPLOY = -1;
    public static final int INTAKE_LEFT_RETRACT = -1;
    public static final int INTAKE_RIGHT_DEPLOY = -1;
    public static final int INTAKE_RIGHT_RETRACT = -1;
    /*
     * Test motors
     */
    public static final int NEO_ID = 5;
    public static final int REDLINE_ID = 6; // Test intake
}
