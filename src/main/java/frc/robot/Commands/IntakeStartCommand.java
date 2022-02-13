package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeStartCommand extends CommandBase {
    private IntakeSubsystem m_intakeSubsystem;

    public IntakeStartCommand(IntakeSubsystem intakeSubsystem) {
        this.m_intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void initialize() {
        this.m_intakeSubsystem.startIntake();
    }
    
    /*
    @Override
    public void execute() {
        this.m_intakeSubsystem.startIntake();
    }
    */

    @Override
    public void end(boolean interrupted) {
        this.m_intakeSubsystem.stopIntake();
    }
}
