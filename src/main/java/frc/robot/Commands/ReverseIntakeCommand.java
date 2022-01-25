package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.IntakeSubsystem;

public class ReverseIntakeCommand extends CommandBase {
    private IntakeSubsystem m_intakeSubsystem;

    public ReverseIntakeCommand(IntakeSubsystem intakeSubsystem) {
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
