package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DriveTrainSubsystem extends SubsystemBase {
    private static final int leftfrontID = 1;
    private static final int leftbackID = 2;
    private static final int rightfrontID = 3;
    private static final int rightbackID = 4;
    private final CANSparkMax leftfrontmotor = new CANSparkMax(leftfrontID, MotorType.kBrushless);
    private final CANSparkMax leftbackmotor = new CANSparkMax(leftbackID, MotorType.kBrushless);
    private final CANSparkMax rightfrontmotor = new CANSparkMax(rightfrontID, MotorType.kBrushless);
    private final CANSparkMax rightbackmotor = new CANSparkMax(rightbackID, MotorType.kBrushless);
    
    

    private final MotorControllerGroup leftControllerGroup = new MotorControllerGroup(leftfrontmotor, leftbackmotor);
    private final MotorControllerGroup rightControllerGroup = new MotorControllerGroup(rightfrontmotor, rightbackmotor);
    private final DifferentialDrive m_drive = new DifferentialDrive(leftControllerGroup, rightControllerGroup);
    
    
    public DriveTrainSubsystem() {
        

        this.leftfrontmotor.restoreFactoryDefaults();
        this.leftbackmotor.restoreFactoryDefaults();
        this.rightfrontmotor.restoreFactoryDefaults();
        this.rightbackmotor.restoreFactoryDefaults();


    }


    public void periodic() {
        
    }

    public void arcadeDrive(double forwardBack, double leftRight) {
        m_drive.arcadeDrive(forwardBack, leftRight);
    }
}

