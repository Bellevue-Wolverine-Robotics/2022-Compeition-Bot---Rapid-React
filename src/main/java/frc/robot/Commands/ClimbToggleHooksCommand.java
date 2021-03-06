package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbToggleHooksCommand extends InstantCommand {
    private ClimbSubsystem m_climbSubsystem;

    public ClimbToggleHooksCommand(ClimbSubsystem climbSubsystem) {
        this.m_climbSubsystem = climbSubsystem;

        this.addRequirements(this.m_climbSubsystem);
    }

    @Override
    public void execute() {
        this.m_climbSubsystem.toggleHooks();
    }
}
