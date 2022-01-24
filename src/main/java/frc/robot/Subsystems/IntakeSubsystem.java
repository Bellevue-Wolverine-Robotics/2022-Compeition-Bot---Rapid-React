package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
    private final VictorSPX m_intakeMotor = new VictorSPX(Constants.REDLINE_ID);

    private boolean m_intakeActivated;

    public IntakeSubsystem() {
        this.m_intakeActivated = false;
    }

    //Lowers arm and activates motor
    public void ToggleIntake() {
        if (!this.m_intakeActivated) {
            this.m_intakeMotor.set(ControlMode.PercentOutput, .5);
            this.m_intakeActivated = true;
        } else {
            this.m_intakeMotor.set(ControlMode.PercentOutput, 0);
            this.m_intakeActivated = false;
        }
    }

    @Override
    public void periodic() {
        
    }
}
