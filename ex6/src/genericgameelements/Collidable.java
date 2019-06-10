package genericgameelements;
import arkanoidelements.Velocity;
import shapes.Ball;
import shapes.Point;
import shapes.Rectangle;

/**
 * The Collidable interface describes methods required for objects that may be hit against
 * in a given game world.
 * @author Benjy Berkowicz & Atara Razin
 *
 */
public interface Collidable {
    /**
     * The collision rectangle of the collidable object.
     * @return the rectangle definition of where the collidable may be hit.
     */
    Rectangle getCollisionRectangle();

    /**
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity.
     * The return is the new velocity expected after the hit (based on
     * the force the object inflicted on us).
     * @param hitter the ball which hit the collidable
     * @param collisionPoint the point at which the object was hit
     * @param currentVelocity the velocity at which the object was hit
     * @return the new calculated velocity.
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}

