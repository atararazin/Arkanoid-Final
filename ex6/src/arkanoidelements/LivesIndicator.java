package arkanoidelements;

import java.awt.Color;

import biuoop.DrawSurface;
import genericgameelements.Counter;
import genericgameelements.Sprite;

/**
 * This class is the Sprite that draws the amount of lives onto the screen. It can
 * draw on the screen and can add it to the list of sprites.
 * @author Atara Razin
 * @author Benjy Berkowicz
 * AKA Batara Berkorazin
 *
 */
public class LivesIndicator implements Sprite {

    private Counter lives;

    /**
     * constructor.
     * @param lives the Counter of lives, keeps track of the number of lives.
     */
    public LivesIndicator(Counter lives) {
        this.lives = lives;
    }

    /**
     * draws the number of lives onto the screen. creates a String representation of the
     * word "Lives:" and then the number of actual lives.
     * @param d the Draw Surface.
     */
    @Override
    public void drawOn(DrawSurface d) {
        //creates new color
        Color textColor = new Color(247, 18, 18);
        d.setColor(textColor);
        //converts the int into a string for printing.
        String text = "Lives: " + Integer.toString(this.lives.getValue());
        d.drawText(100, 24, text, 22);
    }

    /**
     * Doesn't do anything. This is just an implemented method from the interface.
     */
    @Override
    public void timePassed(double dt) {

    }

    /**
     * adds the LivesIndicator to the List of Sprites.
     * @param g the game levels
     */
    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}