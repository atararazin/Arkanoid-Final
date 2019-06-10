package arkanoidelements;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import biuoop.DialogManager;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import biuoop.Sleeper;
import genericgameelements.AnimationRunner;
import genericgameelements.KeyPressStoppableAnimation;
import levelfilereaders.LevelSetReader;
import menuelements.HighScoresAnimation;
import menuelements.HighScoresTable;
import menuelements.HighScoresTask;
import menuelements.Menu;
import menuelements.MenuAnimation;
import menuelements.QuitTask;
import menuelements.Task;

/**
 * The main class, receives a file as command line arguments. Runs the menu.
 * @author Benjy Berkowicz & Atara Razin
 *
 */
public class Ass6Game {
    /**
     * The mainline, receives a file as command line arguments. This runs the menu and
     * thats how the whole thing begins.
     * @param args file
     *
     */
    public static void main(String[] args) {
        //makes the gui
        GUI gui = new GUI("Arkanoid - Batara Berkorazin", GameLevel.maxWidth(), GameLevel.maxHeight());
        KeyboardSensor keyboard = gui.getKeyboardSensor();
        Sleeper sleeper = new Sleeper();
        DialogManager dialog = gui.getDialogManager();
        AnimationRunner runner = new AnimationRunner(gui, sleeper, 60);
        File highscoresFile = new File("highscores.ser");
        String levelFile;

        //checks if it recived a file as command line arguments
        if (args.length == 1) {
            levelFile = args[0];
        } else {
            levelFile = "level_sets.txt";
        }

        HighScoresTable highscores = openFile(highscoresFile);
        GameFlow game = new GameFlow(runner, keyboard, dialog, highscores, highscoresFile);
        MenuAnimation<Task<Void>> menu = new MenuAnimation<Task<Void>>("Main Menu By Batara Berkorazin", keyboard);

        KeyPressStoppableAnimation spacePressedHighscores =
                new KeyPressStoppableAnimation(keyboard, KeyboardSensor.SPACE_KEY, new HighScoresAnimation(highscores));
        Map<String, String> parsedLevelSetFile = LevelSetReader.stringMapFromLevelFile(levelFile);
        Menu<Task<Void>> levelSetsSubMenu = LevelSetReader.menuFromStringMap(parsedLevelSetFile, game, keyboard);

        //adds the different options to the menu
        menu.addSubMenu("s", "Start Game", levelSetsSubMenu);
        menu.addSelection("h", "Show Highscores", new HighScoresTask(runner, spacePressedHighscores));
        menu.addSelection("q", "Quit", new QuitTask());

        //runs the menu loop
        while (true) {
            runner.run(menu);
            Task<Void> task = menu.getStatus();
            task.run();

            menu.resetMenu();
            spacePressedHighscores.reset();
            game = new GameFlow(runner, keyboard, dialog, highscores, highscoresFile);
            levelSetsSubMenu = LevelSetReader.menuFromStringMap(parsedLevelSetFile, game, keyboard);

            menu.updateSubMenu("s", levelSetsSubMenu);
        }
    }

    /**
     * opens the file using a try catch in case it cant.
     * @param highscoresFile the file with the highscores.
     * @return a highscorestable
     */
    private static HighScoresTable openFile(File highscoresFile) {
        HighScoresTable highscores = HighScoresTable.loadFromFile(highscoresFile, 10);
        try {
            highscores.save(highscoresFile);
        } catch (IOException e) {
            System.err.println("Failed closing file: " + highscoresFile);
        }
        return highscores;
    }
}