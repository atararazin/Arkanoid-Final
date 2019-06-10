package genericgameelements;
import java.util.ArrayList;
import java.util.List;

import shapes.Line;
import shapes.Point;
import shapes.Rectangle;

/**
 * GameEnvironment class handles all the elements of a particular game instance.
 * @author Benjy Berkowicz & Atara Razin
 *
 */
public class GameEnvironment {
   //members
   private List<Collidable> collidableList;

   /**
    * the construtor.
    */
   public GameEnvironment() {
       this.collidableList = new ArrayList<Collidable>();
   }

   /**
    * Adds the given collidable to the environment. Saves it in the List.
    *
    *@param c (a new collidable)
    */
   public void addCollidable(Collidable c) {
       this.collidableList.add(c);
   }

   /**
    * Removes a given collidable from the environment.
    * @param c the collidable to be removed.
    */
   public void removeCollidable(Collidable c) {
       this.collidableList.remove(c);
   }

   /**
    * Assume an object moving from line.start() to line.end().
    * If this object will not collide with any of the collidables
    * in this collection, return null. Else, return the information
    * about the closest collision that is going to occur.
    * @param trajectory the line to be checked for collision
    * @return the CollisionInfo including collision point and the collided object
    */
   public CollisionInfo getClosestCollision(Line trajectory) {
       if (this.collidableList.isEmpty()) {
           return null;
       }

       // Initialization for the 'min' function to follow.
       Point closestCollid = null;
       Collidable closestObject = null;

       //goes through the elements in the collidable List
       for (Collidable nextCollid : this.collidableList) {

           //assigns the next object as the current rectangle that is being evaluated
           Rectangle nextCollisBox = nextCollid.getCollisionRectangle();

           //gets the current rectangle's closest intersection point.
           Point nextCollisPoint = trajectory.closestIntersectionToStartOfLine(nextCollisBox);

           // If the collision exists:
           if (null != nextCollisPoint) {
               // If there has been no collision previously, put this as the closest collision
               if (null == closestCollid) {
                   closestCollid = nextCollisPoint;
                   closestObject = nextCollid;
               // If there has been a previous collision, check to see if this one was closer
               } else if (nextCollisPoint.distance(trajectory.start()) < closestCollid.distance(trajectory.start())) {
                   closestCollid = nextCollisPoint;
                   closestObject = nextCollid;
               }
           }
       }
       // If there was a collision, return the information
       if (null != closestCollid) {
           return new CollisionInfo(closestCollid, closestObject);
       } else {
           return null;
       }
   }
}