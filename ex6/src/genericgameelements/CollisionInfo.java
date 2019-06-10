package genericgameelements;
import shapes.Point;

/**
* This class is the way of accessing the information about the collision. Using this class, we can
* find out where the collision occured and with what object.
*
* @author Benjy Berkowicz & Atara Razin
*/
public class CollisionInfo {
    //members
    private Point hitPoint;
    private Collidable hitObject;

    /**
     * constructor.
     * @param hitPoint (the point where the hit occured).
     * @param hitObject (the object with which the collision occurred).
     */
    public CollisionInfo(Point hitPoint, Collidable hitObject) {
        this.hitObject = hitObject;
        this.hitPoint = hitPoint;
    }

    /**
     * Returns the point at which the collision occurs.
     * @return hitPoint (the collision point).
     */
   public Point collisionPoint() {
       return this.hitPoint;
   }

   /**
    * Returns the the collidable object involved in the collision.
    * @return hitObject (the collision object).
    */
   public Collidable collisionObject() {
       return this.hitObject;
   }
}