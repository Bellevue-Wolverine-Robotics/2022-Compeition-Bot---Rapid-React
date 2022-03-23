package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.IntakeSubsystem.IntakeDirection;

public class InputManager {
    private final Joystick m_joystick1 = new Joystick(Constants.JOYSTICK_1);
	private final Joystick m_joystick2 = new Joystick(Constants.JOYSTICK_2);
	private final Joystick m_joystick3 = new Joystick(Constants.JOYSTICK_3);

    private final Joystick m_driveJoystick = this.m_joystick1;
    private final Joystick m_climbJoystick = this.m_joystick3;
    private final Joystick m_intakeJoystick = this.m_joystick2;

    private final RobotMap m_robotMap;

    private final IntakeStartCommand m_intakeStartCommand;
    private final IntakeReverseCommand m_intakeReverseCommand;
    private final IntakeStopCommand m_intakeStopCommand;

    public InputManager(RobotMap robotMap) {
        this.m_robotMap = robotMap;

        this.m_intakeStartCommand = new IntakeStartCommand(this.m_robotMap.getIntake());
        this.m_intakeReverseCommand = new IntakeReverseCommand(this.m_robotMap.getIntake());
        this.m_intakeStopCommand = new IntakeStopCommand(this.m_robotMap.getIntake());

        configureDefaultCommands();
        configureButtonBindings();
    } 

    public void periodic() {
        switch (this.m_intakeJoystick.getPOV()) {
            case 315:
            case 0:
            case 45:
                // If pov held forward
                if (this.m_robotMap.getIntake().getIntakeDirection() != IntakeDirection.Reverse) {
                    this.m_intakeReverseCommand.schedule();
                }
                break;
            case 135:
            case 180:
            case 225:
                // If pov help backwards
                if (this.m_robotMap.getIntake().getIntakeDirection() != IntakeDirection.Forward) {
                    this.m_intakeStartCommand.schedule();
                }
                break;
            default:
                // If pov held literally anywhere else
                if (this.m_robotMap.getIntake().getIntakeDirection() != IntakeDirection.Stop) {
                    this.m_intakeStopCommand.schedule();
                }
                break;
        }
    }
    
    private void configureDefaultCommands() {
        ArcadeDriveJoystickCommand command = new ArcadeDriveJoystickCommand(this.m_robotMap.getDriveTrain(), this.m_driveJoystick);
        /* Test of command based robot control */
        this.m_robotMap.getDriveTrain().setDefaultCommand(command);
    }

    private void configureButtonBindings() {
        // Notice the difference between assignActionToButtonPress and assignActionToButtonHold

        // Climb
        ClimbSubsystem climb = this.m_robotMap.getClimb();

        assignActionToButtonHold(new ClimbArmExtendCommand(climb), Constants.LONG_ARM_EXTEND_BUTTON, this.m_climbJoystick);
        assignActionToButtonHold(new ClimbArmRetractCommand(climb), Constants.LONG_ARM_RETRACT_BUTTON, this.m_climbJoystick);
        // assignActionToButtonHold(new ClimbArmPivotCommand(climb), Constants.LONG_ARM_PIVOT_BUTTON, this.m_climbJoystick);
        // assignActionToButtonHold(new ClimbArmPivotReverseCommand(climb), Constants.LONG_ARM_PIVOT_REVERSE_BUTTON, this.m_climbJoystick);
        assignActionToButtonPress(() -> climb.toggleHooks(), Constants.HOOKS_TOGGLE_BUTTON, this.m_climbJoystick);
        assignActionToButtonPress(() -> { 
            climb.setPivotOverride(true);
            climb.setExtendOverride(true);
        }, Constants.LONG_ARM_OVERRIDE_BUTTON, this.m_climbJoystick);
        assignActionToButtonRelease(() -> { 
            climb.setPivotOverride(false);
            climb.setExtendOverride(false);
        }, Constants.LONG_ARM_OVERRIDE_BUTTON, this.m_climbJoystick);
        assignActionToButtonPress(() -> climb.setAutoRetractHooks(true), Constants.LONG_ARM_AUTO_HOOK_BUTTON, this.m_climbJoystick);
        assignActionToButtonRelease(() -> climb.setAutoRetractHooks(false), Constants.LONG_ARM_AUTO_HOOK_BUTTON, this.m_climbJoystick);

        // Intake
        IntakeSubsystem intake = this.m_robotMap.getIntake();

        // // Add start to press and stop to raise
        // assignActionToButtonPress(new IntakeStartCommand(intake), Constants.INTAKE_START_BUTTON, this.m_intakeJoystick);
        // assignActionToButtonRelease(new IntakeStopCommand(intake), Constants.INTAKE_START_BUTTON, this.m_intakeJoystick);

        // // Do the same for reverse command
        // assignActionToButtonPress(new IntakeReverseCommand(intake), Constants.INTAKE_REVERSE_BUTTON, this.m_intakeJoystick);
        // assignActionToButtonRelease(new IntakeStopCommand(intake), Constants.INTAKE_REVERSE_BUTTON, this.m_intakeJoystick);
        
        assignActionToButtonPress(new IntakeArmToggleCommand(intake), Constants.INTAKE_HOLD_BUTTON, this.m_intakeJoystick);
        assignActionToButtonRelease(new IntakeArmToggleCommand(intake), Constants.INTAKE_HOLD_BUTTON, this.m_intakeJoystick);
        assignActionToButtonPress(new IntakeArmToggleCommand(intake), Constants.INTAKE_TOGGLE_BUTTON, this.m_intakeJoystick);
    }

    private JoystickButton assignActionToButtonPress(Command command, int buttonId, Joystick joystick) {
        JoystickButton button = new JoystickButton(joystick, buttonId);
        button.whenPressed(command);
        return button;
    }

    private JoystickButton assignActionToButtonPress(Runnable action, int buttonId, Joystick joystick) {
        JoystickButton button = new JoystickButton(joystick, buttonId);
        button.whenPressed(action);
        return button;
    }

    private JoystickButton assignActionToButtonRelease(Command command, int buttonID, Joystick joystick) {
        JoystickButton button = new JoystickButton(joystick, buttonID);
        button.whenReleased(command);
        return button;
    }

    private JoystickButton assignActionToButtonRelease(Runnable action, int buttonID, Joystick joystick) {
        JoystickButton button = new JoystickButton(joystick, buttonID);
        button.whenReleased(action);
        return button;
    }

    private JoystickButton assignActionToButtonHold(Command command, int buttonId, Joystick joystick) {
        JoystickButton button = new JoystickButton(joystick, buttonId);
        button.whenHeld(command);
        return button;
    }
}
