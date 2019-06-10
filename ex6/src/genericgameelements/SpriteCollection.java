package genericgameelements;

import java.util.ArrayList;
import java.util.List;

import biuoop.DrawSurface;

/**
 * This class keeps tabs on the sprites. It has a List of all sprites and has
 * the ability to add a sprite, to draw all sprites on the surface and to notify
 * all sprites that time passed
 *
 * @author Benjy Berkowicz & Atara Razin
 */
public class SpriteCollection {
    private List<Sprite> spriteList = new ArrayList<Sprite>();

    /**
     * Adds a new sprite to the List of sprites.
     *
     * @param s
     *            (a new sprite to add to the List).
     */
    public void addSprite(Sprite s) {
        this.spriteList.add(s);
    }

    /**
     * Removes a sprite from the list.
     *
     * @param s
     *            sprite to be removed
     */
    public void removeSprite(Sprite s) {
        this.spriteList.remove(s);
    }

    /**
     * Call timePassed() on all sprites.
     *
     * @param dt
     *            the time that has passed
     */
    public void notifyAllTimePassed(double dt) {
        // We create a copy of the list to prevent crashes when a sprite is
        // removed during iteration
        List<Sprite> copyList = new ArrayList<Sprite>(this.spriteList);
        for (Sprite nextSprite : copyList) {
            nextSprite.timePassed(dt);
        }
    }

    /**
     * Draws all the sprites on the surface.
     *
     * @param d
     *            (the drawSurface).
     */
    public void drawAllOn(DrawSurface d) {
        for (Sprite nextSprite : this.spriteList) {
            nextSprite.drawOn(d);
        }
    }
}