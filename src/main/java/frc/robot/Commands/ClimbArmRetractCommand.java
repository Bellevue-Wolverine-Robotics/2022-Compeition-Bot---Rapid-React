package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbArmRetractCommand extends CommandBase {
    private ClimbSubsystem m_climbSubsystem;

    public ClimbArmRetractCommand(ClimbSubsystem climbSubsystem) {
        this.m_climbSubsystem = climbSubsystem;
    }

    @Override
    public void execute() {
        this.m_climbSubsystem.retractArm();
    }

    @Override
    public void end(boolean interrupted) {
        this.m_climbSubsystem.stopArm();
    }
}
