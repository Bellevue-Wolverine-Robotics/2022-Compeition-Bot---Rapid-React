package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.IntakeSubsystem;

public class IntakeStartCommand extends CommandBase {
    private IntakeSubsystem m_intakeSubsystem;

    public IntakeStartCommand(IntakeSubsystem intakeSubsystem) {
        this.m_intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void execute() {
        this.m_intakeSubsystem.startIntake();
    }

    @Override
    public void end(boolean interrupted) {
        this.m_intakeSubsystem.stopIntake();
    }
}
