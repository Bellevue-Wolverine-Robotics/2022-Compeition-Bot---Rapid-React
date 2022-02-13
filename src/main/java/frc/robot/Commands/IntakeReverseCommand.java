package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeReverseCommand extends CommandBase {
    private IntakeSubsystem m_intakeSubsystem;

    public IntakeReverseCommand(IntakeSubsystem intakeSubsystem) {
        this.m_intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void execute() {
        this.m_intakeSubsystem.reverseIntake();
    }

    @Override
    public void end(boolean interrupted) {
        this.m_intakeSubsystem.stopIntake();
    }
}
