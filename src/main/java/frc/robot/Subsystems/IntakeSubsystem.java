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

    private final float m_amperagePercentThreshold = 1.2f;
    private boolean m_hasIntakedBall = false;
    private long m_timeStartIntake = Long.MAX_VALUE;
    private final int m_rampupTime = 1000; // in ms

    private final int m_rollingAverageSamples = 8;
    private final double[] m_rollingAverageArray = new double[this.m_rollingAverageSamples]; 
    private double m_averageAmperage = 0;

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

        double currentStatorCurrent = Math.abs(this.m_intakeMotor.getStatorCurrent());
        
        // We can use the average amperage of the motor and see if the current amperage is a certain percent over that
        // We only check if the average if the time since starting the motor is above the rampup time
        if (currentStatorCurrent > this.m_averageAmperage * this.m_amperagePercentThreshold
            && System.currentTimeMillis() - this.m_timeStartIntake > this.m_rampupTime) {
            this.m_hasIntakedBall = true;
        } else {
            this.m_hasIntakedBall = false;
        }
        
        // then we figure out the average amperage over the last 5 periodic loops
        // to do that we shift all the elements in the array that stores the amperage for the last 5 loops
        // and store the new elements' sum to average later
        double sum = 0;
        for (int i = this.m_rollingAverageArray.length - 2; i >= 0; i--) {
            this.m_rollingAverageArray[i + 1] = this.m_rollingAverageArray[i];
            sum += this.m_rollingAverageArray[i + 1];
        }

        // Then we add the current amperage to the array and sum
        this.m_rollingAverageArray[0] = currentStatorCurrent;
        sum += currentStatorCurrent;

        // Then we calculate average
        this.m_averageAmperage = sum / this.m_rollingAverageArray.length;
    }

    public void startIntake() {
        this.m_intakeMotor.set(ControlMode.PercentOutput, m_motorSpeed);
        this.m_timeStartIntake = System.currentTimeMillis();
    }

    public void reverseIntake() {
        this.m_intakeMotor.set(ControlMode.PercentOutput, -m_motorSpeed);
        this.m_timeStartIntake = System.currentTimeMillis();
    }

    public void stopIntake() {
        this.m_intakeMotor.set(ControlMode.PercentOutput, 0.0);
        this.m_timeStartIntake = Long.MAX_VALUE;
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
