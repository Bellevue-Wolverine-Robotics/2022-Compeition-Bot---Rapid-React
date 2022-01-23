package frc.robot.Subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
    private final VictorSPX m_intakeMotor = new VictorSPX(Constants.REDLINE_ID);

    private boolean intakeActivated;

    public IntakeSubsystem() {
        intakeActivated = false;
    }

    //Lowers arm and activates motor
    public void ToggleIntake() {
        if (intakeActivated == false) {
            m_intakeMotor.set(ControlMode.PercentOutput, .5);
            intakeActivated = true;
        } else {
            m_intakeMotor.set(ControlMode.PercentOutput, 0);
            intakeActivated = false;
        }
    }

    @Override
    public void periodic() {
        
    }
}
