package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.IMotorController;
import edu.wpi.first.wpilibj.Encoder;
import com.ctre.phoenix.motorcontrol.can.TalonSRX.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
    private final IMotorController m_intakeMotor = new TalonSRX(Constants.INTAKE_MOTOR);
    private final float m_motorSpeed = 0.3f;

    private final Encoder intake_encoder = new Encoder() 



    private final DoubleSolenoid m_leftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.INTAKE_LEFT_DEPLOY, Constants.INTAKE_LEFT_RETRACT);
    private final DoubleSolenoid m_rightSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.INTAKE_RIGHT_DEPLOY, Constants.INTAKE_RIGHT_RETRACT);
    private boolean m_isArmRaised = true;

    public IntakeSubsystem() {
        stopIntake();
        setArmPosition(true);
        this.intake_encoder.configFactoryDefault(); 
    }

    @Override
    public void periodic() {
        
    }

    public void startIntake() {
        this.m_intakeMotor.set(ControlMode.PercentOutput, m_motorSpeed);
    }

    public void reverseIntake() {
        this.m_intakeMotor.set(ControlMode.PercentOutput, -m_motorSpeed);
    }

    public void stopIntake() {
        this.m_intakeMotor.set(ControlMode.PercentOutput, 0.0);
    }
    
    public void toggleArm() {
        this.m_isArmRaised = !this.m_isArmRaised;

        setArmPosition(this.m_isArmRaised);
    }

    public void setArmPosition(boolean raiseArm) {
        Value direction = raiseArm ? Value.kReverse : Value.kForward;

        this.m_leftSolenoid.set(direction);
        this.m_rightSolenoid.set(direction);
    }
}
