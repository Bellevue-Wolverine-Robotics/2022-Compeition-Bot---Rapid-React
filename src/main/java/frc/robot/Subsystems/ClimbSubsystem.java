package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimbSubsystem extends SubsystemBase {
    private final IMotorController m_longArmExtendMotor = new WPI_TalonSRX(Constants.LONG_ARM_EXTEND_MOTOR);
    private final float m_longArmExtendMotorSpeed = 0.3f;
    
    private final CANSparkMax m_longArmPivotMotor = new CANSparkMax(Constants.LONG_ARM_PIVOT_MOTOR, MotorType.kBrushless);
    private final float m_longArmPivotMotorSpeed = 0.3f;

    private final DoubleSolenoid m_smallArmPiston1 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.HOOKS_1_DEPLOY, Constants.HOOKS_1_RETRACT);
    private final DoubleSolenoid m_smallArmPiston2 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.HOOKS_2_DEPLOY, Constants.HOOKS_2_RETRACT);
    private boolean m_areHooksRetracted = true;

    public ClimbSubsystem() {
        setHookPosition(true);
        this.m_longArmPivotMotor.restoreFactoryDefaults();
        this.m_longArmPivotMotor.getEncoder().setPosition(0);
    }

    @Override 
    public void periodic() {

    }

    public void extendArm() {
        this.m_longArmExtendMotor.set(ControlMode.PercentOutput, this.m_longArmExtendMotorSpeed);
    }

    public void retractArm() {
        this.m_longArmExtendMotor.set(ControlMode.PercentOutput, -this.m_longArmExtendMotorSpeed);
    }

    public void stopArm() {
        this.m_longArmExtendMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void pivotArm() {
        this.m_longArmPivotMotor.set(this.m_longArmPivotMotorSpeed);
    }

    public void pivotArmReverse() {
        this.m_longArmPivotMotor.set(-this.m_longArmPivotMotorSpeed);
    }

    public void pivotArmStop() {
        this.m_longArmPivotMotor.set(0.0);
    }

    public void toggleHooks() {
        this.m_areHooksRetracted = !this.m_areHooksRetracted;

        setHookPosition(this.m_areHooksRetracted);
    }

    public void setHookPosition(boolean retractHooks) {
        Value direction = retractHooks ? Value.kReverse : Value.kForward;

        this.m_smallArmPiston1.set(direction);
        this.m_smallArmPiston2.set(direction);
    }
}
