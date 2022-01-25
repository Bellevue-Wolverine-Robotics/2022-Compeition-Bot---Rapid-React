package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
    private final IMotorController m_intakeMotor = new VictorSPX(Constants.REDLINE_ID);

    private IntakeDirection m_intakeDirection = IntakeDirection.STOP;
    private float m_motorSpeed = 1;

    private final DoubleSolenoid m_leftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.INTAKE_LEFT_DEPLOY, Constants.INTAKE_LEFT_RETRACT);
    private final DoubleSolenoid m_rightSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.INTAKE_RIGHT_DEPLOY, Constants.INTAKE_RIGHT_RETRACT);

    private boolean m_isArmRaised = true;

    public IntakeSubsystem() {

    }

    public void startIntake() {
        this.m_intakeMotor.set(ControlMode.PercentOutput, m_motorSpeed);
        this.m_intakeDirection = IntakeDirection.FORWARD;
    }

    public void reverseIntake() {
        this.m_intakeMotor.set(ControlMode.PercentOutput, -m_motorSpeed);
        this.m_intakeDirection = IntakeDirection.REVERSE;
    }

    public void stopIntake() {
        this.m_intakeMotor.set(ControlMode.PercentOutput, 0.0);
        this.m_intakeDirection = IntakeDirection.STOP;
    }

    public void toggleArm() {
        this.m_isArmRaised = !this.m_isArmRaised;

        if (this.m_isArmRaised) {
            this.m_leftSolenoid.set(Value.kReverse);
            this.m_rightSolenoid.set(Value.kReverse);
        } else {
            this.m_leftSolenoid.set(Value.kForward);
            this.m_rightSolenoid.set(Value.kForward);
        }
    }

    public void setMotorSpeed(float speed) {
        this.m_motorSpeed = speed;

        switch (this.m_intakeDirection) {
            case FORWARD:
                this.startIntake();
                break;
            case REVERSE:
                this.reverseIntake();
                break;
            case STOP:
                this.stopIntake();
                break;
            default:
                break;
        }
    }

    @Override
    public void periodic() {
        
    }

    public enum IntakeDirection {
        FORWARD,
        REVERSE,
        STOP
    }
}
