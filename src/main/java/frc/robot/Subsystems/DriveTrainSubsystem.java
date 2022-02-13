package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrainSubsystem extends SubsystemBase {

    private final CANSparkMax m_leftFrontMotor = new CANSparkMax(Constants.LEFT_FRONT, MotorType.kBrushless);
    private final CANSparkMax m_leftBackMotor = new CANSparkMax(Constants.LEFT_BACK, MotorType.kBrushless);
    private final CANSparkMax m_rightFrontMotor = new CANSparkMax(Constants.RIGHT_FRONT, MotorType.kBrushless);
    private final CANSparkMax m_rightBackMotor = new CANSparkMax(Constants.RIGHT_BACK, MotorType.kBrushless);
    
    private final MotorControllerGroup m_leftControllerGroup = new MotorControllerGroup(m_leftFrontMotor, m_leftBackMotor);
    private final MotorControllerGroup m_rightControllerGroup = new MotorControllerGroup(m_rightFrontMotor, m_rightBackMotor);
    private final DifferentialDrive m_drive = new DifferentialDrive(m_leftControllerGroup, m_rightControllerGroup);
    
    public DriveTrainSubsystem() {
        this.m_leftFrontMotor.restoreFactoryDefaults();
        this.m_leftBackMotor.restoreFactoryDefaults();
        this.m_rightFrontMotor.restoreFactoryDefaults();
        this.m_rightBackMotor.restoreFactoryDefaults();
        
        this.m_leftFrontMotor.getEncoder().setPosition(0);
        this.m_leftBackMotor.getEncoder().setPosition(0);
        this.m_rightFrontMotor.getEncoder().setPosition(0);
        this.m_rightBackMotor.getEncoder().setPosition(0);

        this.m_leftFrontMotor.setIdleMode(IdleMode.kBrake);
        this.m_leftBackMotor.setIdleMode(IdleMode.kBrake);
        this.m_rightFrontMotor.setIdleMode(IdleMode.kBrake);
        this.m_rightBackMotor.setIdleMode(IdleMode.kBrake);

        // Because these motors are facing the other direction
        this.m_rightControllerGroup.setInverted(true);

        // Set the position conversion of the encoders so we can do distances
        this.m_leftFrontMotor.getEncoder().setPositionConversionFactor(Constants.POSITION_FACTOR);
        this.m_leftBackMotor.getEncoder().setPositionConversionFactor(Constants.POSITION_FACTOR);
        this.m_rightFrontMotor.getEncoder().setPositionConversionFactor(Constants.POSITION_FACTOR);
        this.m_rightBackMotor.getEncoder().setPositionConversionFactor(Constants.POSITION_FACTOR);
    }

    @Override
    public void periodic() {
        
    }

    public void arcadeDrive(double forwardBack, double leftRight) {
        m_drive.arcadeDrive(forwardBack, leftRight);
    }

    public MotorControllerGroup getLeftControllerGroup() {
        return m_leftControllerGroup;
    }

    public MotorControllerGroup getRightControllerGroup() {
        return m_rightControllerGroup;
    }

    public CANSparkMax getLeftFrontMotor() {
        return m_leftFrontMotor;
    }

    public CANSparkMax getLeftBackMotor() {
        return m_leftBackMotor;
    }

    public CANSparkMax getRightFrontMotor() {
        return m_rightFrontMotor;
    }

    public CANSparkMax getRightBackMotor() {
        return m_rightBackMotor;
    }
}

