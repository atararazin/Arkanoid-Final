package levelfilereaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arkanoidelements.GameFlow;
import biuoop.KeyboardSensor;
import menuelements.Menu;
import menuelements.MenuAnimation;
import menuelements.PlayGameTask;
import menuelements.Task;

public class LevelSetReader {
    public static Map<String, String> stringMapFromLevelFile(String file) {

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(file.trim());
        InputStreamReader reader = new InputStreamReader(is);
        Map<String, String> levelSetMap = new TreeMap<String, String>();
        LineNumberReader buffy = new LineNumberReader(reader);
        String mapInfo = null;
        String fileName = null;

        while (true) {
            String readLine;
            try {
                readLine = buffy.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if (readLine == null) {
                break;
            }
            if (buffy.getLineNumber() % 2 == 1) {
                mapInfo = readLine;
            } else {
                fileName = readLine.trim();
                levelSetMap.put(mapInfo, fileName);
            }
        }

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error closing level set file");
        }
        return levelSetMap;

    }

    public static Menu<Task<Void>> menuFromStringMap(Map<String, String> descriptionToFileMap, GameFlow game,
            KeyboardSensor keyboard) {
        Pattern regex;
        Matcher matcher;
        String key;
        String description;
        regex = Pattern.compile("(\\w):(.*)");
        MenuAnimation<Task<Void>> subLevelMenu = new MenuAnimation<Task<Void>>("Select Level Pack", keyboard);
        for (String levelDescription : descriptionToFileMap.keySet()) {
            matcher = regex.matcher(levelDescription);
            matcher.find();

            key = matcher.group(1);
            description = matcher.group(2);
            String filename = descriptionToFileMap.get(levelDescription).trim();

            Task<Void> task = new PlayGameTask(game, filename);
            subLevelMenu.addSelection(key, description, task);
        }
        return subLevelMenu;
    }
}
