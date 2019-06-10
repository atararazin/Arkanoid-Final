package levelfilereaders;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorParser {
    public static java.awt.Color colorFromString(String s) {
        boolean found = false;
        Color returningColor = null;
        String[] colorList = {"black", "blue", "cyan", "gray", "lightGray", "green",
                              "orange", "pink", "red", "white", "yellow"};
        for (String oneColor : colorList) {
            if (oneColor.equals(s)) {
                found = true;
                Field field = null;
                try {
                    field = Color.class.getField(oneColor);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                Color color = null;
                try {
                    color = (Color) field.get(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                returningColor = color;
            }
        }
        
        if (!found) {
            Pattern pattern = Pattern.compile("RGB\\((\\d+),(\\d+),(\\d+)\\)?");
            Matcher m = pattern.matcher(s);
            m.find();
            returningColor = new Color(Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
        }
        
        return returningColor;
    }
}
