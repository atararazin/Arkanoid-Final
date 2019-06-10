package arkanoidelements;

import biuoop.DrawSurface;

import biuoop.KeyboardSensor;
import genericgameelements.Collidable;
import genericgameelements.Sprite;
import shapes.Ball;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;

/**
 * The paddle is a rectangle that gets printed and knows how to bounce an object
 * off of its top surface. Further, the paddle bounces the collided object based
 * on the x-position that the object hits it at.
 * 
 * @author Benjy Berkowicz & Atara Razin
 *
 */
public class Paddle implements Sprite, Collidable {
    private KeyboardSensor keyboard;
    private java.awt.Color color;
    private int maxWidth;
    private int width;
    private int height;
    private int x;
    private int y;
    private int speed;

    /**
     * The constructor takes all necessary parameters needed to constuct the
     * paddle.
     * 
     * @param x
     *            start x position
     * @param y
     *            start y position
     * @param width
     *            width of the paddle
     * @param height
     *            height of the paddle (only for printing)
     * @param maxWidth
     *            the maxWidth of the window in which the paddle exists
     * @param keyboard
     *            a keyboard sensor to get information about movement
     * @param speed
     *            the speed of the paddle.
     */
    public Paddle(int x, int y, int width, int height, int maxWidth, KeyboardSensor keyboard, int speed) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.maxWidth = maxWidth;
        this.keyboard = keyboard;
        this.speed = speed;
        this.color = java.awt.Color.GRAY;
    }

    /**
     * can change the value of x.
     * 
     * @param newX
     *            the new value.
     */
    public void setX(int newX) {
        this.x = newX;
    }

    /**
     * Moves the paddle left 3 pixels, but not less than 10 (the screen edge).
     */
    public void moveLeft(double movementDelta) {
        this.x = Math.max(this.x - (int) movementDelta, 10);
    }

    /**
     * Moves the paddle right 3 pixels, but not more than the screen Width - 10
     * (the screen edge).
     */
    public void moveRight(double movementDelta) {
        this.x = Math.min(this.x + (int) movementDelta, maxWidth - this.width - 10);
    }

    /**
     * Every frame we check to see if the keyboard left/right buttons are
     * pressed, and based on that move the paddle to the left or right.
     */
    @Override
    public void timePassed(double dt) {
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            this.moveLeft(dt * this.speed);
        }
        if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            this.moveRight(dt * this.speed);
        }
    }

    /**
     * Draws the paddle (essentially a coloured rectangle with a black border).
     * 
     * @param surface
     *            the surface on which to draw the paddle
     */
    @Override
    public void drawOn(DrawSurface surface) {

        // The outer black-bordered rectangle
        surface.setColor(java.awt.Color.BLACK);
        surface.fillRectangle(this.x, this.y, this.width, this.height);

        // The inner coloured rectangle
        surface.setColor(this.color);
        surface.fillRectangle(this.x + 1, this.y + 1, this.width - 2, this.height - 2);
    }

    /**
     * Returns the collision rectangle of the paddle. This is much smaller than
     * the printed rectangle, as only the top surface is collidable!
     * 
     * @return the collision rectangle
     */
    @Override
    public Rectangle getCollisionRectangle() {
        Point topLeft = new Point(this.x, this.y);
        return new Rectangle(topLeft, this.width, 1);
    }

    /**
     * The hit function defines movement after the object is hit. This uses math
     * fomulae to break down the paddle into 5 parts, and multiply velocity
     * based on the x-coord of the hit location.
     * 
     * @param collisionPoint
     *            the collision point of the object
     * @param currentVelocity
     *            the current velocity of the object
     * @param hitter
     *            the ball that got hit
     * @return the new calculated velocity
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        Line oneLine;

        // We check to see if we have hit the top of the paddle.
        oneLine = this.getCollisionRectangle().componentLines().get(0);
        if (oneLine.isOnLine(collisionPoint)) {
            // We get the x-position of the point where the collision happened
            // as a function of the width of the paddle
            double baseHitPos = collisionPoint.getX() - oneLine.start().getX();

            // The multiplier is the 0-4 (5 possibilties) section of the
            // baseHitPosition in terms of the width
            long multiplier = Math.round(baseHitPos / (this.width / 4));

            double currentDx = currentVelocity.getDeltaX();
            double currentDy = currentVelocity.getDeltaY();
            // To find our current speed we use the pythagoras equation
            double currentSpeed = Math.sqrt(Math.pow(currentDx, 2) + Math.pow(currentDy, 2));

            // Our new velocity retains our previous speed and uses the
            // multiplier to generate the angle
            Velocity newVeloc = Velocity.fromAngleAndSpeed(300 + (multiplier * 30), currentSpeed);
            currentVelocity = newVeloc;
        }

        return currentVelocity;
    }

    /**
     * Add this paddle to the game.
     * 
     * @param g
     *            the game to be added
     */
    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }
}