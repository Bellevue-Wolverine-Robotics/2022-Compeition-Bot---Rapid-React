package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSensorSubsystem extends SubsystemBase {
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(Port.kOnboard);
    private final ColorMatch m_colorMatcher = new ColorMatch();
    
    public static final Color BLUE = new Color(0.26, 0.47, 0.26);
    public static final Color RED = new Color(0.67, 0.29, 0.04);

    public ColorSensorSubsystem() {
        addMatchColor(BLUE);
        addMatchColor(RED);

        this.m_colorMatcher.setConfidenceThreshold(0.9);
    }

    public void addMatchColor(Color color) {
        this.m_colorMatcher.addColorMatch(color);
    }

    public ColorMatchResult getClosestMatch() {
        return this.m_colorMatcher.matchClosestColor(this.getColor());
    }

    public ColorMatchResult getMatch() {
        return this.m_colorMatcher.matchColor(this.getColor());
    }

    public DriverStation.Alliance getMatchColorName() {
        ColorMatchResult result = this.getMatch();
        if (result == null) {
            return DriverStation.Alliance.Invalid;
        }
        
        if (result.color == RED) {
            return DriverStation.Alliance.Red;
        } else if (result.color == BLUE) {
            return DriverStation.Alliance.Blue;
        } else {
            return DriverStation.Alliance.Invalid;
        }
    }

    public boolean isDetectedColorOurAlliance() {
        return getMatchColorName() == DriverStation.getAlliance();
    }

    public Color getColor() {
        return this.m_colorSensor.getColor();
    }

    public ColorSensorV3 getColorSensor() {
        return this.m_colorSensor;
    }
}
