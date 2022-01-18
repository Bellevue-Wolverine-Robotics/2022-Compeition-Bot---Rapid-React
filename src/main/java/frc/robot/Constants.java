package frc.robot;

public class Constants {

	public static final int JOYSTICK_1 = 0;
	public static final int JOYSTICK_2 = 1;
	public static final int JOYSTICK_3 = 2;

	public static final int QUICKTURN_BUTTON = 1;

    public static final int LEFT_FRONT_ID = 1;
    public static final int LEFT_BACK_ID = 2;
    public static final int RIGHT_FRONT_ID = 3;
    public static final int RIGHT_BACK_ID = 4;

    public static final int WHEEL_SIZE = 6;
    public static final double GEAR_RATIO = 1 / 10.71;
    // Calculate the position factor by calculating the circumference of the wheel times the gear ratio
    // From https://dev.revrobotics.com/sparkmax/software-resources/migrating-ctre-to-rev#change-units-from-rotations-to-inches
    public static final double POSITION_FACTOR = Math.PI * WHEEL_SIZE * GEAR_RATIO;
}
