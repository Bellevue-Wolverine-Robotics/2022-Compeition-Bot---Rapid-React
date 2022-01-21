package frc.robot.Subsystems;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSensorSubsystem extends SubsystemBase {
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(Port.kOnboard);
    private final ColorMatch m_colormatcher = new ColorMatch();
    
    public boolean matchColor(Color color) {
        ColorMatchResult result = getColor();
        return result != null && result.color == color;
    }

    public ColorMatchResult getColor() {
        return this.m_colormatcher.matchColor(this.m_colorSensor.getColor());
    }

    public ColorSensorV3 getColorSensor() {
        return this.m_colorSensor;
    }
}
