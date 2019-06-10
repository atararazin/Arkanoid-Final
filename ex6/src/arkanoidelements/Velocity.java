package arkanoidelements;
import shapes.Point;

/**
 * The velocity class defines movement along the x and y axes of a given object.  It can be defined raw by
 * the user in terms of x/y change, or calculated by angle/speed into x/y change.
 * @author Benjy Berkowicz & Atara Razin
 */
public class Velocity {
   // The members dx and dy are all that are relevant to velocity for these purposes (2d field)
   private double dx;
   private double dy;

   /**
    * Velocity constructor, takes two parameters and assigns them to class members.
    * @param dx the change in x per step
    * @param dy the change in y per step
    */
   public Velocity(double dx, double dy) {
       this.dx = dx;
       this.dy = dy;
   }

   // Accessors

   /**
    * getDeltaX returns the x change per step.
    * @return the dx member of the velocity object
    */
   public double getDeltaX() {
       return this.dx;
   }

   /**
    * getDeltaY returns the y change per step.
    * @return the dy member of the velocity object
    */
   public double getDeltaY() {
       return this.dy;
   }

   /**
    * Manually changes the dx member of a given velocity object.
    * @param newDx the new x-change to be set
    */
   public void setDeltaX(double newDx) {
       this.dx = newDx;
   }

   /**
    * Manually changes the dy member of a given velocity object.
    * @param newDy the new y-change to be set
    */
   public void setDeltaY(double newDy) {
       this.dy = newDy;
   }

   /**
    * The static method is used because a constructor that takes two doubles is already being used.
    * The function uses sin and cos to translate an angle into a change on the x and y axis.
    * @param angle the angle of movement
    * @param speed the speed of movement
    * @return a Velocity object generated based on the input parameters
    */
   public static Velocity fromAngleAndSpeed(double angle, double speed) {
       // The angle has 90 taken from in (to make it so 0 points up as specs. demand)
       double angleInRadians = Math.toRadians(angle - 90);
       // The angle (converted to radians for use in sin and cos) is converted into dx and dy respectively
       double dx = Math.cos(angleInRadians) * speed;
       double dy = Math.sin(angleInRadians) * speed;
       // A new object is created and returned
       return new Velocity(dx, dy);
    }

    /**
     * Given a point p, applies the given velocity to it (by adding delta x and y to it).
     * @param p the point to be adjusted.
     * @param dt the amount of time that has passed.
     * @return a new Point generated based on the movement specified by the velocity.
     */
   public Point applyToPoint(Point p, double dt) {
       return new Point(p.getX() + (this.dx * dt), p.getY() + (this.dy * dt));
   }
}