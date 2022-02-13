package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeArmToggleCommand extends InstantCommand {
    private IntakeSubsystem m_intakeSubsystem;

    public IntakeArmToggleCommand(IntakeSubsystem intakeSubsystem) {
        this.m_intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void execute() {
        this.m_intakeSubsystem.toggleArm();
    }
}
