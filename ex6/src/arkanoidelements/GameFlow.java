package arkanoidelements;

import java.io.File;

import java.io.IOException;
import java.util.List;

import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import genericgameelements.AnimationRunner;
import genericgameelements.Counter;
import genericgameelements.EndgameScreen;
import genericgameelements.KeyPressStoppableAnimation;
import menuelements.HighScoresAnimation;
import menuelements.HighScoresTable;
import menuelements.ScoreInfo;

/**
 * controls the game flow, runs each level, checks if there are no lives and if
 * enough blocks have been destroyed. If we won, it prints the won screen.
 * 
 * @author Atara Razin
 * @author Benjy Berkowicz AKA Batara Berkorazin
 *
 */
public class GameFlow {

    private AnimationRunner animation;
    private KeyboardSensor keyboard;
    private DialogManager dialog;
    private Counter livesCounter;
    private Counter scoreCounter;
    private boolean youWon;
    private HighScoresTable highscores;
    private File highscoresFile;

    /**
     * constructor.
     * 
     * @param ar
     *            the animation
     * @param ks
     *            the keyboard sensor.
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, DialogManager dialog, HighScoresTable highscores,
            File highscoresFile) {
        this.animation = ar;
        this.keyboard = ks;
        this.livesCounter = new Counter();
        this.scoreCounter = new Counter();
        this.dialog = dialog;
        this.highscores = highscores;
        this.highscoresFile = highscoresFile;
    }

    /**
     * Runs a series of provided levels (given in LevelInformation format),
     * keeping track of its endgame conditions, and terminating when they are
     * reached.
     * 
     * @param levels
     *            a list of LevelInformations
     */
    public void runLevels(List<LevelInformation> levels) {

        // start the game with seven lives
        this.livesCounter.increase(7);
        for (LevelInformation levelInfo : levels) {
            // creates the new level
            GameLevel level = new GameLevel(levelInfo, this.keyboard, this.animation, this.livesCounter,
                    this.scoreCounter);

            level.initialize();

            // plays the turn until either enough blocks have been removed or
            // there are no lives left
            while ((this.livesCounter.getValue() > 0) && !level.destroyedEnoughBlocks()) {
                level.playOneTurn();
            }

            // break out of the loop if there are no lives left,meaning you lost
            if (this.livesCounter.getValue() <= 0) {
                break;
            }
        }

        // do the endgame screen
        this.youWon = this.livesCounter.getValue() > 0;
        EndgameScreen endScreen = new EndgameScreen(this.youWon, this.scoreCounter);
        this.animation.run(new KeyPressStoppableAnimation(this.keyboard, KeyboardSensor.SPACE_KEY, endScreen));

        if (highscores.getRank(this.scoreCounter.getValue()) <= highscores.size()) {
            String name = dialog.showQuestionDialog("High Score!", "Enter Your Name:", "");
            ScoreInfo newScore = new ScoreInfo(name, this.scoreCounter.getValue());
            highscores.add(newScore);
        }

        HighScoresAnimation scoreList = new HighScoresAnimation(highscores);
        this.animation.run(new KeyPressStoppableAnimation(this.keyboard, KeyboardSensor.SPACE_KEY, scoreList));
        try {
            highscores.save(highscoresFile);
        } catch (IOException e) {
            System.err.println("Failed closing file: " + highscoresFile);
        }
    }
    
    public void reset() {
        this.livesCounter = new Counter();
        this.scoreCounter = new Counter();
    }
}