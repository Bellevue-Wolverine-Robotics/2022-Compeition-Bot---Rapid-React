package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbArmPivotCommand extends CommandBase {
    private final ClimbSubsystem m_climbSubsystem;

    public ClimbArmPivotCommand(ClimbSubsystem climbSubsystem) {
        this.m_climbSubsystem = climbSubsystem;

        this.addRequirements(this.m_climbSubsystem);
    }

    @Override
    public void execute() {
        this.m_climbSubsystem.pivotArm();
    }

    @Override
    public void end(boolean interrupted) {
        this.m_climbSubsystem.pivotArmStop();
    }
}
