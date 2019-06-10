package genericgameelements;

import biuoop.DrawSurface;

/**
 * The PauseScreen animation displays text until the player specifies it should continue by pressing space.
 * @author Benjy Berkowicz
 * @author Atara Razin
 *
 */
public class PauseScreen implements Animation {

    /**
     * Performs one frame, which are all identical and consist simply of text on the screen.
     * @param d the draw surface to be drawn on
     * @param dt the time that has passed
     */
    public void doOneFrame(DrawSurface d, double dt) {
       d.drawText(150, d.getHeight() / 2, "paused -- press space to continue", 32);
       // When a space key is detected, the terminating value boolean is adjusted.
    }

    /**
     * The animation is supposed to stop when the stop boolean has been set to true.
     * @return should the pausescreen keep displaying or not.
     */
    public boolean shouldStop() {
        return false;
    }
 }