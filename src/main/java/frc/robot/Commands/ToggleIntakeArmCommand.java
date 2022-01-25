package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Subsystems.IntakeSubsystem;

public class ToggleIntakeArmCommand extends InstantCommand {
    private IntakeSubsystem m_intakeSubsystem;

    public ToggleIntakeArmCommand(IntakeSubsystem intakeSubsystem) {
        this.m_intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void execute() {
        this.m_intakeSubsystem.toggleArm();
    }
}
