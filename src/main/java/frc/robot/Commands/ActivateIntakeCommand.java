package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Subsystems.IntakeSubsystem;

public class ActivateIntakeCommand extends InstantCommand {
    private IntakeSubsystem m_intakeSubsystem;

    public ActivateIntakeCommand(IntakeSubsystem intakeSubsystem) {
        m_intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void execute() {
        m_intakeSubsystem.ToggleIntake();
    }
}
