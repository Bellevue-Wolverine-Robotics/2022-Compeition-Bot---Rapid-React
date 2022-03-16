package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimbSubsystem extends SubsystemBase {
    private final WPI_TalonSRX m_longArmExtendMotor = new WPI_TalonSRX(Constants.LONG_ARM_EXTEND_MOTOR);
    private final float m_longArmExtendMotorSpeed = 0.5f;
    private final float m_longArmRetractMotorSpeed = 0.7f;
    private final DigitalInput m_longArmExtendLimitSwitch = new DigitalInput(1);
    
    private final WPI_TalonSRX m_longArmPivotMotor = new WPI_TalonSRX(Constants.LONG_ARM_PIVOT_MOTOR);
    private final float m_longArmPivotMotorSpeed = 0.4f;

    private final WPI_TalonSRX m_smallArmMotor1 = new WPI_TalonSRX(Constants.SMALL_ARM_1_MOTOR);
    private final WPI_TalonSRX m_smallArmMotor2 = new WPI_TalonSRX(Constants.SMALL_ARM_2_MOTOR);
    private final float m_smallArmMotorSpeedLeft = 0.5f;
    private final float m_smallArmMotorSpeedRight = 0.9f;
    private final int m_timeToToggleHooks = 500; // Time in MS it takes to toggle the hooks
    private long m_timeStartedTogglingHooks;
    private boolean m_areHooksRetracted = true;

    public ClimbSubsystem() {
        // Make sure hooks are retracted
        setHookPosition(true);

        // Configure extend motor
        this.m_longArmExtendMotor.configFactoryDefault();
        this.m_longArmExtendMotor.setNeutralMode(NeutralMode.Brake);
        this.resetLongArmExtendEncoder();

        // Configure pivot motor
        this.m_longArmPivotMotor.configFactoryDefault();
        this.m_longArmPivotMotor.setSelectedSensorPosition(0);
        this.m_longArmPivotMotor.setNeutralMode(NeutralMode.Brake);
        // Negative is regular pivot, and positive and reverse pivot
        this.m_longArmPivotMotor.setInverted(true);

        // Configure small arm motors
        SupplyCurrentLimitConfiguration currentLimitConfig = new SupplyCurrentLimitConfiguration();

        // Limited current
        currentLimitConfig.currentLimit = 6;
        // Max current
        currentLimitConfig.triggerThresholdCurrent = 6;
        // Max time before setting to limited current
        currentLimitConfig.triggerThresholdTime = 0.3;
        currentLimitConfig.enable = true;

        this.m_smallArmMotor1.setNeutralMode(NeutralMode.Brake);
        this.m_smallArmMotor1.configSupplyCurrentLimit(currentLimitConfig);

        this.m_smallArmMotor2.setNeutralMode(NeutralMode.Brake);
        this.m_smallArmMotor2.configSupplyCurrentLimit(currentLimitConfig);
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
                    this.pivotStopArm();
                }
                break;
            case -1:
                if (!this.canArmPivotReverse()) {
                    this.pivotStopArm();
                }
                break;
            default:
                break;
        }

        // Have a timer to make sure hooks are only running for the listed time
        if (System.currentTimeMillis() - this.m_timeStartedTogglingHooks >= this.m_timeToToggleHooks) {
            stopHooks();
        }
    }

    public void resetLongArmExtendEncoder() {
        this.m_longArmExtendMotor.setSelectedSensorPosition(0);
    }

    public void extendArm() {
        if (this.canArmExtend()) {
            this.m_longArmExtendMotor.set(this.m_longArmExtendMotorSpeed);
        } else {
            this.stopArm();
        }
    }

    public void retractArm() {
        retractArm(false);
    }

    // USE THIS WITH EXTREME CAUTION
    public void retractArm(boolean override) {
        if (override || this.canArmRetract()) {
            this.m_longArmExtendMotor.set(-this.m_longArmRetractMotorSpeed);
        } else {
            this.stopArm();
        }
    }

    public void stopArm() {
        this.m_longArmExtendMotor.stopMotor();
    }

    public boolean canArmExtend() {
        return this.getArmExtendDistance() + Constants.ARM_EXTENSION_DEADZONE <= Constants.MAX_ARM_EXTENSION;
    }

    public boolean canArmRetract() {
        // As a safety precaution, the arm cannot retract while the limit switch is held
        return this.getArmExtendDistance() - Constants.ARM_EXTENSION_DEADZONE >= 0 && !this.m_longArmExtendLimitSwitch.get();
    }

    public double getArmExtendDistance() {
        // Divide by 1024 because CTRE uses 0-4096 as a full rotation
        return Math.abs(this.m_longArmExtendMotor.getSelectedSensorPosition() * Constants.ARM_EXTEND_POSITION_FACTOR / 4096);
    }

    public DigitalInput getArmExtendLimitSwitch() {
        return this.m_longArmExtendLimitSwitch;
    }

    public void resetLongArmPivotEncoder() {
        this.m_longArmPivotMotor.setSelectedSensorPosition(0);
    }

    public void pivotArm() {
        if (this.canArmPivot()) {
            this.m_longArmPivotMotor.set(this.m_longArmPivotMotorSpeed);
        } else {
            this.pivotStopArm();
        }
    }

    public void pivotArmReverse() {
        pivotArmReverse(false);
    }

    // USE THIS WITH EXTREME CAUTION
    public void pivotArmReverse(boolean override) {
        if (override || this.canArmPivotReverse()) {
            this.m_longArmPivotMotor.set(-this.m_longArmPivotMotorSpeed);
        } else {
            this.pivotStopArm();
        }
    }

    public void pivotArmStop() {
        this.m_longArmPivotMotor.stopMotor();
    }

    public boolean canArmPivot() {
        return this.getArmPivotPosition() + Constants.ARM_PIVOT_DEADZONE <= Constants.MAX_ARM_PIVOT;
    }

    public boolean canArmPivotReverse() {
        // For safety, the arm cannot pivot backwards when the limit switch is held
        return this.getArmPivotPosition() - Constants.ARM_PIVOT_DEADZONE >= 0 && !this.m_longArmPivotLimitSwitch.get();
    }

    public double getArmPivotPosition() {
        return Math.abs(this.m_longArmPivotMotor.getSelectedSensorPosition() * Constants.ARM_EXTEND_POSITION_FACTOR / 4096);
    }

    public DigitalInput getArmPivotLimitSwitch() {
        return this.m_longArmPivotLimitSwitch;
    }

    public void toggleHooks() {
        this.m_areHooksRetracted = !this.m_areHooksRetracted;

        setHookPosition(this.m_areHooksRetracted);
    }

    public void stopHooks() {
        this.m_smallArmMotor1.stopMotor();
        this.m_smallArmMotor2.stopMotor();
    }

    public void setHookPosition(boolean retractHooks) {
        // This code assumes that inverted retracts the hooks
        // If not then we can change it
        // Small arm motor one is mounted the other direction to 2 so it needs to be inverted differently
        this.m_smallArmMotor1.setInverted(!retractHooks);
        this.m_smallArmMotor2.setInverted(retractHooks);

        this.m_smallArmMotor1.set(this.m_smallArmMotorSpeedRight);
        this.m_smallArmMotor2.set(this.m_smallArmMotorSpeedLeft);

        this.m_timeStartedTogglingHooks = System.currentTimeMillis();
    }
}
