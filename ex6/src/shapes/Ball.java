package shapes;
import java.awt.Color;
import java.util.List;

import arkanoidelements.GameLevel;
import arkanoidelements.Velocity;
import biuoop.DrawSurface;
import genericgameelements.CollisionInfo;
import genericgameelements.GameEnvironment;
import genericgameelements.Sprite;

/**
 * The ball class defines a filled circle of a given color that is aware of the game environment
 * within which it exists.
 * The ball is able to handle the physics of hitting collidables on the given game environment,
 * and may print itself.
 *
 * @author Benjy Berkowicz & Atara Razin
 */
public class Ball implements Sprite {
   private Point center;
   private int radius;
   private Color color;
   // The velocity is initialized to 0,0 by default (the user doesn't need to set them)
   private Velocity ballVelocity = new Velocity(0, 0);
   // The game environment MUST be provided within the constructor.
   private GameEnvironment game;

   // Constructors
   /**
    * The main constructor for the ball - providing starting x, y coordinates, its radius
    * its colour and the game environment upon which it exists.
    *
    * @param x starting x position
    * @param y starting y position
    * @param r radius
    * @param color color of the ball
    * @param game the game environment on which the ball travels
    */
   public Ball(int x, int y, int r, java.awt.Color color, GameEnvironment game) {
       this.radius = r;
       //Creates a new 'Point' for the center of the ball
       this.center = new Point(x, y);
       this.color = color;
       this.game = game;
   }

   // Basic Accessors

   /**
    * getX returns the x coordinate of the center of the ball.
    * @return the x coordinate of the center of the ball.
    */
   public int getX() {
       //Casting to int is done because Point's getX function returns a double.
       return (int) this.center.getX();
   }

   /**
    * getY returns the y coordinate of the center of the ball.
    * @return the y coordinate of the center of the ball.
    */
   public int getY() {
       //Casting to int is done because Point's getY function returns a double.
       return (int) this.center.getY();
   }

   /**
    * getSize returns the radius of the ball.
    * @return the radius of the ball
    */
   public int getSize() {
       return this.radius;
   }

   /**
    * getColor returns the color of the ball as a java color object.
    * @return the color of the ball.
    */
   public java.awt.Color getColor() {
       return this.color;
   }

   /**
    * getVelocity returns the current velocity of the ball.
    * @return the velocity of the ball as a Velocity object.
    */
   public Velocity getVelocity() {
       return this.ballVelocity;
   }

   // General Methods
   /**
    * Adds itself to the list of sprites in the game.
    *
    * @param g (the Game we are working with).
    */
   public void addToGame(GameLevel g) {
       g.addSprite(this);
   }

   /**
    * Removes the ball from the sprites list.
    *
    * @param g the game level
    */
   public void removeFromGame(GameLevel g) {
       g.removeSprite(this);
   }


   /**
    * timePassed handles the movement of a ball for one 'frame' according to its velocity and the bounds
    * of its movement.  When the ball is detected as hitting a collidable on the field, its velocity
    * is recalculated and its position is adjusted to be outside of the collidable which it hit.
    * @param dt the time that has passed
    */
   public void timePassed(double dt) {

       Point newCenter = this.getVelocity().applyToPoint(this.center, dt);
       // The 'collision trajectory' is a line spanning the new distance the ball is to have travelled
       Line trajectory = new Line(this.center, newCenter);

       // The generated trajectory is now used to see if the ball has collided with anything.
       CollisionInfo nextHit = game.getClosestCollision(trajectory);

       // If there was no collision detected, we move the ball as normal.
       if (nextHit == null) {
           this.center = newCenter;
       } else {
           /*
            * We first adjust the X/Y position of the ball as needed.  This is done by detecting which
            * side of the collision rectangle has been hit and moving the center of the ball to a point just 'touching'
            * the collision rectangle, but not inside it.
            * To find which side we hit, we break the rectangle up into its component lines and see on which one we had
            * a collision.
            */

           Point hitSpot = nextHit.collisionPoint();
           List<Line> collisionLines = nextHit.collisionObject().getCollisionRectangle().componentLines();

           // Case 1: We hit the 'horizontal top' line of the rectangle, therefore adjust the Y coordinate.
           Line oneLine = collisionLines.get(0);
           if (oneLine.isOnLine(hitSpot)) {
               this.center.setY(hitSpot.getY() - radius);
           }

           // Case 2: We hit the 'horizontal bottom' line of the rectangle, therefore adjust the Y coordinate.
           oneLine = collisionLines.get(1);
           if (oneLine.isOnLine(hitSpot)) {
               this.center.setY(hitSpot.getY() + radius);
           }

           // Case 3: We hit the 'vertical left' line of the rectangle, therefore adjust the X coordinate.
           oneLine = collisionLines.get(2);
           if (oneLine.isOnLine(hitSpot)) {
               this.center.setX(hitSpot.getX() - radius);
           }

           // Case 4: We hit the 'vertical right' line of the rectangle, therefore adjust the X coordinate.
           oneLine = collisionLines.get(3);
           if (oneLine.isOnLine(hitSpot)) {
               this.center.setX(hitSpot.getX() + radius);
           }

           // After adjusting the ball position, we now adjust the ball velocity after the hit.
           this.ballVelocity = nextHit.collisionObject().hit(this, hitSpot, this.ballVelocity);
       }
   }

    /**
     * Draws the ball on the provided DrawSurface. Generally this method is called after moveOneStep.
     * @param surface the surface for the ball to be drawn on.
     */
   public void drawOn(DrawSurface surface) {
       surface.setColor(Color.WHITE);
       surface.fillCircle(this.getX(), this.getY(), this.radius);

       surface.setColor(this.color);
       // We call our accessors to find the dimensions needed for printing.
       surface.fillCircle(this.getX(), this.getY(), this.radius - 1);
   }

   /**
    * Used to set the velocity of the ball given an independantly created Velocity object.
    * @param v the new velocity of the ball.
    */
   public void setVelocity(Velocity v) {
       this.ballVelocity = v;
   }

   /**
    * Used to set the velocity of the ball using a delta X and delta Y parameters.
    * @param dx the change in x position of the ball per step movement
    * @param dy the change in y position of the ball per step movement
    */
   public void setVelocity(double dx, double dy) {
       this.ballVelocity.setDeltaX(dx);
       this.ballVelocity.setDeltaY(dy);
   }
}