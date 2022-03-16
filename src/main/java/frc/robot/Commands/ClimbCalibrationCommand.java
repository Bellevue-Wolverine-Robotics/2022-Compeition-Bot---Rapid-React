package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.ClimbSubsystem;

public class ClimbCalibrationCommand extends CommandBase {
    private ClimbSubsystem m_climbSubsystem;
    
    private boolean m_finishedPivot = false;
    private boolean m_finishedExtend = false;

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
        this.m_climbSubsystem.retractArm(true);
        this.m_climbSubsystem.pivotArmReverse(true);

        // If the arm has retracted all the way, set the encoder position to 0 and stop the motor
        if ((!this.m_finishedExtend && this.m_climbSubsystem.getArmExtendLimitSwitch().get())
            || this.m_climbSubsystem.getArmExtendDistance() > Constants.MAX_ARM_EXTENSION) {
            this.m_climbSubsystem.stopArm();
            this.m_climbSubsystem.resetLongArmExtendEncoder();
            this.m_finishedExtend = true;
        }

        if ((!this.m_finishedPivot && this.m_climbSubsystem.getArmPivotLimitSwitch().get())
            || this.m_climbSubsystem.getArmPivotPosition() > Constants.MAX_ARM_PIVOT) {
            this.m_climbSubsystem.pivotStopArm();
            this.m_climbSubsystem.resetLongArmPivotEncoder();
            this.m_finishedPivot = true;
        }
    }

    @Override
    public boolean isFinished() {
        return this.m_finishedExtend && this.m_finishedPivot;
    }
}
