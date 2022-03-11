package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbCalibrationCommand extends CommandBase {
    private ClimbSubsystem m_climbSubsystem;
    private boolean m_isFinished = false;

    public ClimbCalibrationCommand(ClimbSubsystem climbSubsystem) {
        this.m_climbSubsystem = climbSubsystem;

        this.addRequirements(this.m_climbSubsystem);
    }

    @Override
    public void initialize() {
        // Start moving arm towards its limits
        this.m_climbSubsystem.retractArm(true);
        this.m_climbSubsystem.pivotArmReverse(true);
    }

    @Override
    public void execute() {
        // If the arm has retracted all the way, set the encoder position to 0
        if (this.m_climbSubsystem.getArmPivotLimitSwitch().get()) {
            this.m_climbSubsystem.stopArm();
            this.m_climbSubsystem.resetLongArmExtendEncoder();
        }

        if (this.m_climbSubsystem.getArmPivotLimitSwitch().get()) {
            this.m_climbSubsystem.pivotStopArm();
            this.m_climbSubsystem.resetLongArmPivotEncoder();
        }
    }

    @Override
    public boolean isFinished() {
        return this.m_isFinished;
    }
}
