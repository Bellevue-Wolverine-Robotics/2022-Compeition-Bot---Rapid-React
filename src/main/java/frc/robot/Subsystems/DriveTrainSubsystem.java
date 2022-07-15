package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotMap;

public class DriveTrainSubsystem extends SubsystemBase {
    private final RobotMap m_robotMap;
    
    private static final CANSparkMax leftFrontMotor = new CANSparkMax(Constants.LEFT_FRONT, MotorType.kBrushless);
    private static final CANSparkMax leftBackMotor = new CANSparkMax(Constants.LEFT_BACK, MotorType.kBrushless);
    private static final CANSparkMax rightFrontMotor = new CANSparkMax(Constants.RIGHT_FRONT, MotorType.kBrushless);
    private static final CANSparkMax rightBackMotor = new CANSparkMax(Constants.RIGHT_BACK, MotorType.kBrushless);
    
    private static final MotorControllerGroup m_leftControllerGroup = new MotorControllerGroup(DriveTrainSubsystem.leftFrontMotor, DriveTrainSubsystem.leftBackMotor);
    private static final MotorControllerGroup m_rightControllerGroup = new MotorControllerGroup(DriveTrainSubsystem.rightFrontMotor, DriveTrainSubsystem.rightBackMotor);
    private static final DifferentialDrive m_drive = new DifferentialDrive(DriveTrainSubsystem.m_leftControllerGroup, DriveTrainSubsystem.m_rightControllerGroup);

    private static DifferentialDriveOdometry odometry;
    private final DifferentialDriveKinematics m_kinematics = new DifferentialDriveKinematics(Constants.TRACK_WIDTH);
    
    public DriveTrainSubsystem(RobotMap robotMap) {
        this.m_robotMap = robotMap;

        DriveTrainSubsystem.leftFrontMotor.restoreFactoryDefaults();
        DriveTrainSubsystem.leftBackMotor.restoreFactoryDefaults();
        DriveTrainSubsystem.rightFrontMotor.restoreFactoryDefaults();
        DriveTrainSubsystem.rightBackMotor.restoreFactoryDefaults();
        
        DriveTrainSubsystem.leftFrontMotor.setIdleMode(IdleMode.kBrake);
        DriveTrainSubsystem.leftBackMotor.setIdleMode(IdleMode.kBrake);
        DriveTrainSubsystem.rightFrontMotor.setIdleMode(IdleMode.kBrake);
        DriveTrainSubsystem.rightBackMotor.setIdleMode(IdleMode.kBrake);

        // Because these motors are facing the other direction
        DriveTrainSubsystem.m_rightControllerGroup.setInverted(true);

        // Set the position conversion of the encoders so we can do distances
        DriveTrainSubsystem.leftFrontMotor.getEncoder().setPositionConversionFactor(Constants.POSITION_FACTOR);
        DriveTrainSubsystem.leftBackMotor.getEncoder().setPositionConversionFactor(Constants.POSITION_FACTOR);
        DriveTrainSubsystem.rightFrontMotor.getEncoder().setPositionConversionFactor(Constants.POSITION_FACTOR);
        DriveTrainSubsystem.rightBackMotor.getEncoder().setPositionConversionFactor(Constants.POSITION_FACTOR);

        resetEncoders();

        DriveTrainSubsystem.odometry = new DifferentialDriveOdometry(this.m_robotMap.getGyro().getRotation2d());
    }

    @Override
    public void periodic() {
        DriveTrainSubsystem.odometry.update(
            this.m_robotMap.getGyro().getRotation2d(),
            DriveTrainSubsystem.leftFrontMotor.getEncoder().getPosition(),
            DriveTrainSubsystem.rightFrontMotor.getEncoder().getPosition());
    }

    public void arcadeDrive(double forwardBack, double leftRight) {
        DriveTrainSubsystem.m_drive.arcadeDrive(forwardBack, leftRight);
    }

    public static void tankDriveVolts(double leftVolts, double rightVolts) {
        DriveTrainSubsystem.m_leftControllerGroup.setVoltage(leftVolts);
        DriveTrainSubsystem.m_rightControllerGroup.setVoltage(rightVolts);
        DriveTrainSubsystem.m_drive.feed();
    }

    public void setMaxOutput(double maxOutput) {
        DriveTrainSubsystem.m_drive.setMaxOutput(maxOutput);
    }

    public void stopMotors() {
        DriveTrainSubsystem.m_drive.stopMotor();
    }

    public MotorControllerGroup getLeftControllerGroup() {
        return DriveTrainSubsystem.m_leftControllerGroup;
    }

    public MotorControllerGroup getRightControllerGroup() {
        return DriveTrainSubsystem.m_rightControllerGroup;
    }

    public CANSparkMax getLeftFrontMotor() {
        return DriveTrainSubsystem.leftFrontMotor;
    }

    public CANSparkMax getLeftBackMotor() {
        return DriveTrainSubsystem.leftBackMotor;
    }

    public CANSparkMax getRightFrontMotor() {
        return DriveTrainSubsystem.rightFrontMotor;
    }

    public CANSparkMax getRightBackMotor() {
        return DriveTrainSubsystem.rightBackMotor;
    }

    public DifferentialDriveKinematics getKinematics() {
        return this.m_kinematics;
    }

    public static Pose2d getPose() {
        return DriveTrainSubsystem.odometry.getPoseMeters();
    }
    
    public static DifferentialDriveWheelSpeeds getWheelSpeeds() {
        // Not sure if having both front, both back, or opposite for this is better
        return new DifferentialDriveWheelSpeeds(DriveTrainSubsystem.leftFrontMotor.getEncoder().getVelocity(), DriveTrainSubsystem.rightFrontMotor.getEncoder().getVelocity());
    }

    public double getAverageEncoderDistance() {
        return (
            DriveTrainSubsystem.leftFrontMotor.getEncoder().getPosition() +
            DriveTrainSubsystem.leftBackMotor.getEncoder().getPosition() +
            DriveTrainSubsystem.rightFrontMotor.getEncoder().getPosition() +
            DriveTrainSubsystem.rightBackMotor.getEncoder().getPosition()
        ) / 4.0;
    }

    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        DriveTrainSubsystem.odometry.resetPosition(pose, this.m_robotMap.getGyro().getRotation2d());
    }

    public void resetEncoders() {
        DriveTrainSubsystem.leftFrontMotor.getEncoder().setPosition(0);
        DriveTrainSubsystem.leftBackMotor.getEncoder().setPosition(0);
        DriveTrainSubsystem.rightFrontMotor.getEncoder().setPosition(0);
        DriveTrainSubsystem.rightBackMotor.getEncoder().setPosition(0);
    }
}

