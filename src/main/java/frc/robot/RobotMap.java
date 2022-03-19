package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import frc.robot.subsystems.*;

/* Creates robot subsystems and commands, binds those commands to triggering 
    events (such as buttons), and specify which commands will run in autonomous. */
public class RobotMap {
    // yes the class name is weird, the docs say it's a misnomer
    // Also put this at least a ft away from the front bumper, also leave it 1-2ft above the ground
    private final AnalogPotentiometer m_ultrasonicSensor = new AnalogPotentiometer(0, Constants.INCHES_PER_5V);

    private final ADXRS450_Gyro m_gyro = new ADXRS450_Gyro();

	private final DriveTrainSubsystem m_driveTrain = new DriveTrainSubsystem();

    private final ClimbSubsystem m_climb = new ClimbSubsystem();

    private final IntakeSubsystem m_intake = new IntakeSubsystem();

    public RobotMap() {
		
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

    public AnalogPotentiometer getUltrasonicSensor() {
        return this.m_ultrasonicSensor;
    }
}
