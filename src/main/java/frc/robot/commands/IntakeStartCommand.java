package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeStartCommand extends InstantCommand {
    private IntakeSubsystem m_intakeSubsystem;

    public IntakeStartCommand(IntakeSubsystem intakeSubsystem) {
        this.m_intakeSubsystem = intakeSubsystem;

        this.addRequirements(this.m_intakeSubsystem);
    }

    @Override
    public void execute() {
        this.m_intakeSubsystem.startIntake();
    }
}
