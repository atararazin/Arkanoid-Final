package arkanoidelements;

import java.awt.Color;

import biuoop.DrawSurface;
import genericgameelements.Counter;
import genericgameelements.Sprite;
/**
 * This class does the actual drawing of the score onto the screen. it can draw on the
 * surface and can add it to the list of sprites in the game.
 * @author Atara Razin
 * @author Benjy Berkowicz
 * AKA Batara Berkorazin
 *
 */
public class ScoreIndicator implements Sprite {
    private Counter gameScore;

    /**
     * constructor.
     * @param gameScore the score Counter
     */
    public ScoreIndicator(Counter gameScore) {
        this.gameScore = gameScore;
    }

    /**
     * Draws the Sprite onto the screen. Makes a color, makes a string form of the word
     * "score" and the actual number and then prints it.
     * @param d the drawsurface that we're drawing on
     */
    @Override
    public void drawOn(DrawSurface d) {
        //creates new color
        Color textColour = new Color(247, 18, 18);
        d.setColor(textColour);
        //converts the int into a string for the text representation.
        String text = "Score: " + Integer.toString(this.gameScore.getValue());
        d.drawText(250, 24, text, 22);
    }

    /**
     * does nothing, it is only here because it's implemented as the method of the Sprite
     * interface.
     */
    @Override
    public void timePassed(double dt) {

    }

    /**
     * adds the ScoreIndicator to the List of Sprites, so that it can too be kept track of.
     * @param g the gamelevel
     */
    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}