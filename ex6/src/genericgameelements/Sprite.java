package genericgameelements;
import arkanoidelements.GameLevel;
import biuoop.DrawSurface;

/**
 * The Sprite interface handles functions required for displaying and adjusting the location
 * of a variety of objects.  Sprites are also able to add themselves to a given game environment.
 * @author Benjy Berkowicz & Atara Razin
 *
 */
public interface Sprite {
   /**
    * drawOn draws the given sprite on the game window.
    * @param d the DrawSurface to be drawn on
    */
   void drawOn(DrawSurface d);

   /**
    * Notify the sprite that time has passed (in order to update its physical parameters if
    * necessary).
    * @param dt the time that has passed
    */
   void timePassed(double dt);

   /**
    * Adds the sprite to a provided Game.
    * @param g the game to which the sprite is added
    */
   void addToGame(GameLevel g);
}