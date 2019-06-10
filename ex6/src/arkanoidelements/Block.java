package arkanoidelements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import biuoop.DrawSurface;
import genericgameelements.Collidable;
import genericgameelements.HitListener;
import genericgameelements.HitNotifier;
import genericgameelements.Sprite;
import levelfilereaders.SpriteMaker;
import shapes.Ball;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;

/**
 * The Block class defines a filled rectangle with a black border that has
 * collision properties as well as drawing enabling properties. The block also
 * knows how to add itself to a given game environment and keep track of the
 * amount of times it has been collided with.
 * 
 * @author Benjy Berkowicz & Atara Razin
 *
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private List<HitListener> hitListeners = new ArrayList<HitListener>();
    private Rectangle rect;
    private int hitPoints;
    private Map<Integer, Sprite> hitsList;
    private Color strokeColor;

    /**
     * The constructor takes characteristics needed for drawing as well as a
     * 'health total' to keep track of the amount of hits it may take.
     * 
     * @param rect
     *            the dimensions of the block in rectangle form
     * @param hitPoints
     *            the amount of times it can be hit
     * @param color
     *            the color of the block
     */
    public Block(Rectangle rect, int hitPoints, Map<Integer, Sprite> hitsList, java.awt.Color strokeColor) {
        this.rect = rect;
        this.strokeColor = strokeColor;
        this.hitPoints = hitPoints;
        this.hitsList = hitsList;
    }

    public Block(Rectangle rect, int hitPoints, java.awt.Color everythingColor) {
        this.rect = rect;
        this.strokeColor = everythingColor;
        this.hitPoints = hitPoints;
        this.hitsList = new TreeMap<Integer, Sprite>();
        Point topleft = rect.getUpperLeft();
        int x = (int) topleft.getX();
        int y = (int) topleft.getY();
        hitsList.put(-1, SpriteMaker.fromColor(everythingColor, x, y, (int) rect.getWidth(), (int) rect.getHeight()));
        this.strokeColor = null;
    }

    
    /**
     * getCollisionRectangle returns the collision shape of the block (which is
     * the whole rectangle upon which it is defined).
     * 
     * @return this.rect the collision rectangle
     */
    public Rectangle getCollisionRectangle() {
        return this.rect;
    }

    /**
     * Informs the block that it has been hit and returns a new velocity for the
     * object that has hit it. The new velocity is calculated based on the hit
     * position. It also notfies that there was a hit.
     * 
     * @param collisionPoint
     *            the point where the collision occured
     * @param currentVelocity
     *            the current velocity of the object that hit the block
     * @param hitter
     *            the ball thats hitting
     * @return currentVelocity the new velocity after the ball has hit it.
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        // The hit is recorded for the block (the minimum amount of 'health'
        // remains 0)
        this.hitPoints = Math.max(this.hitPoints - 1, 0);
        Line oneLine;

        // Case 1: We hit the 'horizontal top' line of the rectangle, therefore
        // we reverse Y velocity.
        oneLine = this.rect.componentLines().get(0);
        if (oneLine.isOnLine(collisionPoint)) {
            currentVelocity.setDeltaY(-1 * Math.abs(currentVelocity.getDeltaY()));
        }

        // Case 2: We hit the 'horizontal bottom' line of the rectangle,
        // therefore we reverse Y velocity.
        oneLine = this.rect.componentLines().get(1);
        if (oneLine.isOnLine(collisionPoint)) {
            currentVelocity.setDeltaY(Math.abs(currentVelocity.getDeltaY()));
        }

        // Case 3: We hit the 'left verticle' line of the rectangle, therefore
        // we reverse X velocity.
        oneLine = this.rect.componentLines().get(2);
        if (oneLine.isOnLine(collisionPoint)) {
            currentVelocity.setDeltaX(-1 * Math.abs(currentVelocity.getDeltaX()));
        }

        // Case 4: We hit the 'right verticle' line of the rectangle, therefore
        // we reverse X velocity.
        oneLine = this.rect.componentLines().get(3);
        if (oneLine.isOnLine(collisionPoint)) {
            currentVelocity.setDeltaX(Math.abs(currentVelocity.getDeltaX()));
        }

        this.notifyHit(hitter);
        return currentVelocity;
    }

    /**
     * Notifies the listeners that there was a hit.
     * 
     * @param hitter
     *            the ball that gets hit.
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * Currently timePassed is unused for the block class however is needed as
     * block implements the sprite interface.
     */
    public void timePassed(double dt) {

    }

    /**
     * drawOn prints the rectangle onto the screen in its given colour with a
     * black border surrounding it. On top of the rectangles is printed the
     * blocks remaining hitpoits and if none remain an X is printed.
     * 
     * @param surface
     *            the surface on which to draw the block.
     */
    public void drawOn(DrawSurface surface) {
        // For line-shortening purposes the printing variables are block
        // initialized first.
        Point corner = this.rect.getUpperLeft();
        int rectHeight = (int) this.rect.getHeight();
        int rectWidth = (int) this.rect.getWidth();
        int x = (int) corner.getX();
        int y = (int) corner.getY();

        // The stroke is first printed
        if (this.strokeColor != null) {
            surface.setColor(this.strokeColor);
            surface.drawRectangle(x, y, rectWidth + 1, rectHeight + 1);
        }
        
        // The above sprite is next displayed (depending on the hitpoints, with
        // -1 being the default image)
        if (this.hitsList.containsKey(this.hitPoints)) {
            this.hitsList.get(this.hitPoints).drawOn(surface);
        } else {
            this.hitsList.get(-1).drawOn(surface);
        }
    }

    /**
     * addToGame is used to add the block as both a collidable and sprite to a
     * game instance.
     * 
     * @param g
     *            the game instance
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * removes the block from the sprite collection and from the collidable
     * collection.
     * 
     * @param g
     *            the game
     */
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
        g.removeCollidable(this);
    }

    /**
     * adds a hit listener to the list of hitlisteners.
     * 
     * @param hl
     *            the hit listener that we want to add.
     */
    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * removes a hit listener from the list of hitlisteners.
     * 
     * @param hl
     *            the hit listener that we want to remove.
     */
    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * accessor for hitPoints.
     * 
     * @return the number of hitpoints.
     */
    public int getHitPoints() {
        return this.hitPoints;
    }
}