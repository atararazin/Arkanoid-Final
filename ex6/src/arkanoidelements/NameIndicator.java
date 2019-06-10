package arkanoidelements;

import java.awt.Color;

import biuoop.DrawSurface;
import genericgameelements.Sprite;
/**
 * This class is the Sprite that draws the level name onto the screen. It can draw on the screen
 * and can add it to the list of sprites.
 * @author Atara Razin
 * @author Benjy Berkowicz
 * AKA Batara Berkorazin
 *
 */
public class NameIndicator implements Sprite {
    private String levelName;
    /**
     * constructor.
     * @param mapName the level's name
     */
    public NameIndicator(String mapName) {
        this.levelName = mapName;
    }

    /**
     * Draws the name on the screen.
     * @param d the draw surface
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.BLACK);
        d.fillRectangle(0, 0, GameLevel.maxWidth(), 30);
        
        //creates new color
        Color textColor = new Color(247, 18, 18);
        d.setColor(textColor);
        String text = "Level Name: " + this.levelName;
        d.drawText(400, 24, text, 22);
    }

    /**
     * does nothing. is implemented because it is a sprite, hence it gets this
     * method from the interface.
     */
    @Override
    public void timePassed(double dt) {

    }

    /**
     * adds this name indicator to the list of Sprites in the game given.
     * @param g the game level.
     */
    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}