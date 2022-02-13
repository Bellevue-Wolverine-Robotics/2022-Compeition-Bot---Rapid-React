package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbArmPivotReverseCommand extends CommandBase {
    private ClimbSubsystem m_climbSubsystem;

    public ClimbArmPivotReverseCommand(ClimbSubsystem climbSubsystem) {
        this.m_climbSubsystem = climbSubsystem;

        this.addRequirements(this.m_climbSubsystem);
    }

    @Override
    public void execute() {
        this.m_climbSubsystem.pivotArmReverse();
    }

    @Override
    public void end(boolean interrupted) {
        this.m_climbSubsystem.pivotArmStop();
    }
}
