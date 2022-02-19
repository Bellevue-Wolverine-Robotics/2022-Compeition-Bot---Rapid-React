// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private RobotMap m_robotMap;

    private AutonomousManager m_autonomousManager;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        CameraServer.startAutomaticCapture();

        CommandScheduler.getInstance().onCommandInitialize((command) -> {
            System.out.println(command.getName() + " intialized.");
        });

        CommandScheduler.getInstance().onCommandFinish((command) -> {
            System.out.println(command.getName() + " finished.");
        });

        CommandScheduler.getInstance().onCommandInterrupt((command) -> {
            System.out.println(command.getName() + " interrupted.");
        });

        this.m_robotMap = new RobotMap(); 

        this.m_autonomousManager = new AutonomousManager(this.m_robotMap);

        // Temp vars for auto
        SmartDashboard.putNumber("motorSpeed", 0.3);                            // This is in percent
        SmartDashboard.putNumber("intakeReverseTimeToScore", 2);                // This is in seconds
        SmartDashboard.putNumber("distanceToDriveAfterIntakeMotorSpeedDrop", 2);// This is in inches
        SmartDashboard.putNumber("turnAroundAngle", 171);                       // This is in degrees
        SmartDashboard.putNumber("distanceFromHubToScore", 10);                 // This is in inches
        SmartDashboard.putNumber("reverseAfterFinished", 141);                  // This is in inches
        SmartDashboard.putNumber("turnAroundAfterScoreAngle", 180);             // This is in degrees
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
        this.m_autonomousManager.autonomousInit();
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
        this.m_autonomousManager.autonomousPeriodic();
    }

    /** This function is called once when teleop is enabled. */
    @Override
    public void teleopInit() {
        this.m_autonomousManager.cancelCommands();
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
