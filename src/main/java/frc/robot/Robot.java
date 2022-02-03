// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.ArcadeDriveDistanceCommand;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
	private RobotMap m_robotMap;

  private static final String DEFAULT_AUTO = "Default";
  private static final String CUSTOM_AUTO = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Command m_autonomousCommand;
  private boolean m_hasStartCurrentCommand = false;
  private int m_autonomousStep = 1;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", DEFAULT_AUTO);
    m_chooser.addOption("My Auto", CUSTOM_AUTO);
    SmartDashboard.putData("Auto choices", m_chooser);

    CameraServer.startAutomaticCapture();

    this.m_robotMap = new RobotMap(); 
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    this.m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      // Ignoring this case for now
      case CUSTOM_AUTO:
        break;
      case DEFAULT_AUTO:
      default:
        switch (this.m_autonomousStep) {
          case 1:
            // check if the command is scheduled, if not then schedule it
            if (!this.m_hasStartCurrentCommand) {
              this.m_autonomousCommand = new ArcadeDriveDistanceCommand(this.m_robotMap.getDriveTrain(), 10, 0.3);
              this.m_autonomousCommand.schedule();
              
              this.m_hasStartCurrentCommand = true;
            } else if (this.m_autonomousCommand.isFinished()) {
              // If it finished then move on
              this.m_autonomousStep++;
              this.m_hasStartCurrentCommand = false;
            }
            break;
          case 2: 
            System.out.println("WEEEEEEE");
            break;
          default:
            System.out.println("Unknown Autonomous Step");
            break;
        }
        break;
    }

		// if (this.m_autonomousCommand != null) {
		// 	this.m_autonomousCommand.schedule();
		// }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    if (this.m_autonomousCommand != null) {
			this.m_autonomousCommand.cancel();
		}
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    CommandScheduler.getInstance().cancelAll();
		this.m_robotMap.onDisable();
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {

  }
}
