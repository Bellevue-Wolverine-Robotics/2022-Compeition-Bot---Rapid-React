package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
    private final WPI_TalonSRX m_intakeMotor = new WPI_TalonSRX(Constants.INTAKE_MOTOR);
    private final float m_motorSpeed = 0.3f;

    private final float m_amperageThreshold = 30;
    private final int m_amperageTimeout = 100; // In ms
    private boolean m_hasIntakedBall = false;
    private long m_startTimeOverThreshold = -1;

    private final DoubleSolenoid m_leftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.INTAKE_LEFT_DEPLOY, Constants.INTAKE_LEFT_RETRACT);
    private final DoubleSolenoid m_rightSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.INTAKE_RIGHT_DEPLOY, Constants.INTAKE_RIGHT_RETRACT);
    private boolean m_isArmRaised = true;

    public IntakeSubsystem() {
        stopIntake();
        setArmPosition(true);
    }

    @Override
    public void periodic() {

        // Figure out if we have intaked a ball
        // We can use the amperage of the intake motor and see if it spikes over a certain threshold for a certain amount of time
        System.out.println(this.m_intakeMotor.getStatorCurrent());
        if (this.m_intakeMotor.getStatorCurrent() > this.m_amperageThreshold) {
            // Only if the start time over threshold value isn't the default should we update it
            if (this.m_startTimeOverThreshold != -1) {
                this.m_startTimeOverThreshold = System.currentTimeMillis();
            }

            // If the current time minus the start time over threshold (amount of time over the threshold) is larger than the time out
            // then we have intaked a ball
            if (System.currentTimeMillis() - this.m_startTimeOverThreshold > m_amperageTimeout) {
                this.m_hasIntakedBall = true;
            } else {
                this.m_hasIntakedBall = false;
            }
        } else {
            this.m_hasIntakedBall = false;
            this.m_startTimeOverThreshold = -1;
        }
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

    public boolean hasIntakedBall() {
        return this.m_hasIntakedBall;
    }
}
