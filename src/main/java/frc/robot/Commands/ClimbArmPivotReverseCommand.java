package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.ClimbSubsystem;

public class ClimbArmPivotReverseCommand extends CommandBase {
    private ClimbSubsystem m_climbSubsystem;

    public ClimbArmPivotReverseCommand(ClimbSubsystem climbSubsystem) {
        this.m_climbSubsystem = climbSubsystem;
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
