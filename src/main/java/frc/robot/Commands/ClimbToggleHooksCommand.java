package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Subsystems.ClimbSubsystem;

public class ClimbToggleHooksCommand extends InstantCommand {
    private ClimbSubsystem m_climbSubsystem;

    public ClimbToggleHooksCommand(ClimbSubsystem climbSubsystem) {
        this.m_climbSubsystem = climbSubsystem;
    }

    @Override
    public void execute() {
        this.m_climbSubsystem.toggleHooks();
    }
}
