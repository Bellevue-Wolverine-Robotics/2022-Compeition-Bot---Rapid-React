package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimbSubsystem extends SubsystemBase {
    private final WPI_TalonSRX m_longArmExtendMotor = new WPI_TalonSRX(Constants.LONG_ARM_EXTEND_MOTOR);
    private final float m_longArmExtendMotorSpeed = 0.5f;
    private final float m_longArmRetractMotorSpeed = 0.7f;
    
    private final WPI_TalonSRX m_longArmPivotMotor = new WPI_TalonSRX(Constants.LONG_ARM_PIVOT_MOTOR);
    private final float m_longArmPivotMotorSpeed = 0.1f;

    private final DoubleSolenoid m_smallArmPiston1 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.HOOKS_1_DEPLOY, Constants.HOOKS_1_RETRACT);
    private final DoubleSolenoid m_smallArmPiston2 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.HOOKS_2_DEPLOY, Constants.HOOKS_2_RETRACT);
    private boolean m_areHooksRetracted = true;

    public ClimbSubsystem() {
        setHookPosition(true);

        this.m_longArmExtendMotor.configFactoryDefault();
        this.m_longArmExtendMotor.setSelectedSensorPosition(0);
        this.m_longArmExtendMotor.setNeutralMode(NeutralMode.Brake);

        this.m_longArmExtendMotor.configFactoryDefault();
        this.m_longArmExtendMotor.setSelectedSensorPosition(0);
        this.m_longArmExtendMotor.setNeutralMode(NeutralMode.Brake);
    }

    @Override 
    public void periodic() {
        // Ensure that the arm can extend/retract if the motors are currently extending or retracting
        switch ((int)this.m_longArmExtendMotor.get()) {
            case 1:
                if (!this.canArmExtend()) {
                    stopArm();
                }
                break;
            case -1:
                if (!this.canArmRetract()) {
                    stopArm();
                }
                break;
            default:
                break;
        }

        // Ensure that the arm can pivot in both directions if the motors are currently running
        switch ((int)this.m_longArmPivotMotor.get()) {
            case 1:
                if (!this.canArmPivot()) {
                    this.pivotArmStop();
                }
                break;
            case -1:
                if (!this.canArmPivotReverse()) {
                    this.pivotArmStop();
                }
                break;
            default:
                break;
        }
    }

    public void extendArm() {
        if (this.canArmExtend()) {
            this.m_longArmExtendMotor.set(this.m_longArmExtendMotorSpeed);
        } else {
            this.stopArm();
        }
    }

    public void retractArm() {
        if (this.canArmRetract()) {
            this.m_longArmExtendMotor.set(-this.m_longArmRetractMotorSpeed);
        } else {
            this.stopArm();
        }
    }

    public void stopArm() {
        this.m_longArmExtendMotor.set(0.0);
    }

    public boolean canArmExtend() {
        return this.getArmExtendDistance() + Constants.ARM_EXTENSION_DEADZONE <= Constants.MAX_ARM_EXTENSION;
    }

    public boolean canArmRetract() {
        return this.getArmExtendDistance() - Constants.ARM_EXTENSION_DEADZONE >= 0;
    }

    public double getArmExtendDistance() {
        // Divide by 1024 because CTRE uses 0-4096 as a full rotation
        return Math.abs(this.m_longArmExtendMotor.getSelectedSensorPosition() * Constants.ARM_EXTEND_POSITION_FACTOR / 4096);
    }

    public void pivotArm() {
        if (this.canArmPivot()) {
            this.m_longArmPivotMotor.set(this.m_longArmPivotMotorSpeed);
        } else {
            this.pivotArmStop();
        }
    }

    public void pivotArmReverse() {
        if (this.canArmPivotReverse()) {
            this.m_longArmPivotMotor.set(-this.m_longArmPivotMotorSpeed);
        } else {
            this.pivotArmStop();
        }
    }

    public void pivotArmStop() {
        this.m_longArmPivotMotor.set(0.0);
    }

    public boolean canArmPivot() {
        return this.getArmPivotPosition() + Constants.ARM_PIVOT_DEADZONE <= Constants.MAX_ARM_PIVOT;
    }

    public boolean canArmPivotReverse() {
        return this.getArmPivotPosition() - Constants.ARM_PIVOT_DEADZONE >= 0;
    }

    public double getArmPivotPosition() {
        return Math.abs(this.m_longArmPivotMotor.getSelectedSensorPosition() * Constants.ARM_EXTEND_POSITION_FACTOR / 4096);
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
