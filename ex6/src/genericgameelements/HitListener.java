package genericgameelements;

import arkanoidelements.Block;
import shapes.Ball;

/**
 * The HitListener interface describes the ability to listen to and handle a collidable being hit event.
 * @author Benjy Berkowicz
 * @author Atara Razin
 *
 */
public interface HitListener {
    /**
     * The method to be called when a given collidable is hit by a ball.
     * @param beingHit the collidable being hit
     * @param hitter the ball hitting it
     */
    void hitEvent(Block beingHit, Ball hitter);
 }
