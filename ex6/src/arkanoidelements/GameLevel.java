package arkanoidelements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import genericgameelements.Animation;
import genericgameelements.AnimationRunner;
import genericgameelements.Collidable;
import genericgameelements.CountdownAnimation;
import genericgameelements.Counter;
import genericgameelements.GameEnvironment;
import genericgameelements.KeyPressStoppableAnimation;
import genericgameelements.PauseScreen;
import genericgameelements.Sprite;
import genericgameelements.SpriteCollection;
import shapes.Ball;
import shapes.Point;
import shapes.Rectangle;

/**
 * The Game class controls on a macro-level the game environment for a given
 * Arkanoid game. The game consists of two balls, a paddle and a series of
 * blocks in an arrangement - with walls to limit the game field.
 *
 * @author Benjy Berkowicz & Atara Razin
 *
 */
public class GameLevel implements Animation {
    // Members
    private SpriteCollection sprites = new SpriteCollection();
    private GameEnvironment environment;
    private Counter blockCounter;
    private Counter ballCounter;
    private Counter scoreCounter;
    private Counter livesCounter;
    private LevelInformation levelInfo;
    private Paddle paddle;
    private int maxWidth;
    private int maxHeight;
    private AnimationRunner runner;
    private boolean running = true;
    private KeyboardSensor keyboard;
    private int totalBlocks = 0;

    /**
     * The constructor. Builds two news Counters, one for the number of Blocks and one for
     * the number of balls. It also creates a new environment. The rest are sent in as
     * parameters.
     * @param levelInfo the level's information
     * @param keyboard the keyboard
     * @param scoreCounter keeps track of the score
     * @param runner the AnimationRunner
     * @param livesCounter keeps track of the amount of lives
     */
    public GameLevel(LevelInformation levelInfo, KeyboardSensor keyboard, AnimationRunner runner, Counter livesCounter,
            Counter scoreCounter) {
        this.maxWidth = GameLevel.maxWidth();
        this.maxHeight = GameLevel.maxHeight();
        this.blockCounter = new Counter();
        this.ballCounter = new Counter();
        this.scoreCounter = scoreCounter;
        this.livesCounter = livesCounter;
        this.runner = runner;
        this.environment = new GameEnvironment();
        this.keyboard = keyboard;
        this.levelInfo = levelInfo;
    }

    /**
     * this method adds a collidable to the list of Collidables.
     *
     * @param c
     *            (the new collidable that we want to add).
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * this method removes a colldiable from the list of Collidables.
     *
     * @param c
     *          (the collidable that we want to remove)
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * this method adds a sprite to the list of sprites.
     *
     * @param s
     *            (the new sprite that we want to add).
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * this method removes a sprite from the list of sprites.
     *
     * @param s
     *            (the sprite that we want to remove).
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * Game initialization. Creation of the playing field as well as blocks /
     * paddles / balls that it includes. Plus, it makes the block remover, listeners for
     * the score and Printers for printing various things onto the screen.
     */
    public void initialize() {

        //gets the background and adds it to game.
        this.levelInfo.getBackground().addToGame(this);

        // The Paddle is created
        int startingXPos = (this.maxWidth / 2) - (this.levelInfo.paddleWidth() / 2);
        int startingYPos = this.maxHeight - 25;
        int paddleWidth = this.levelInfo.paddleWidth();
        this.paddle = new Paddle(startingXPos, startingYPos, paddleWidth, 15, this.maxWidth,
                this.keyboard, levelInfo.paddleSpeed());
        paddle.addToGame(this);

        // The border walls are created in their own function for clarity's
        // sake.
        this.makeBorderWalls();

        //creates a Listener for the score.
        ScoreTrackingListener score = new ScoreTrackingListener(this.scoreCounter);
        //creates a remover for the Blocks
        BlockRemover remover = new BlockRemover(this, this.blockCounter);
        //creates something that prints the name of the level on the screen.
        NameIndicator namePrinter = new NameIndicator(this.levelInfo.levelName());
        namePrinter.addToGame(this);
        //creates something that prints the number of lives on the screen.
        LivesIndicator livesPrinter = new LivesIndicator(this.livesCounter);
        livesPrinter.addToGame(this);
        //creates something that prints the score on the screen.
        ScoreIndicator scorePrinter = new ScoreIndicator(this.scoreCounter);
        scorePrinter.addToGame(this);

        //creates the blocks
        for (Block b : this.levelInfo.blocks()) {
            // After the rectangle of dimensions has been created, the box
            // is ready to be added to the game.
            b.addHitListener(remover);
            b.addHitListener(score);
            b.addToGame(this);
            this.blockCounter.increase(1);
            this.totalBlocks++;
        }
    }

    /**
     * creates the balls, sets the paddle in the starting place, run the countdown animantion
     * sets running as true and then runs the actual level.
     */
    public void playOneTurn() {
        this.createBalls();
        //sets the paddle
        this.paddle.setX((this.maxWidth / 2) - (this.levelInfo.paddleWidth() / 2));
        //runs the countdown animantion, while sending in a new countdown.
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        //the animation runs while the boolean is true. Hence, we are assigning it to true.
        this.running = true;
        //runs the level animation
        this.runner.run(this);
        return;
    }

    /**
     * makeBorderWalls is a private function that handles the tedious
     * preparation work needed to make the 4 blocks that make up the
     * walls of the game. This cannot be handled procedurally (using loops) as
     * the dimensions of the four walls are unique.
     */
    private void makeBorderWalls() {
       
        // Top horizontal wall
        Point leftCorner = new Point(0, 30);
        Rectangle newRect = new Rectangle(leftCorner, this.maxWidth, 10);
        Block thisBlock = new Block(newRect, 0, java.awt.Color.BLACK);
        thisBlock.addToGame(this);

        // Left vertical wall
        leftCorner = new Point(0, 30);
        newRect = new Rectangle(leftCorner, 10, this.maxHeight);
        thisBlock = new Block(newRect, 0, java.awt.Color.BLACK);
        thisBlock.addToGame(this);

        /*
         * Bottom horizontal wall - Has a special "BallRemover" listener to
         * delete balls. Also, it is not drawn on the screen as its top-corner
         * is outside of the game world.
         */
        leftCorner = new Point(-20, this.maxHeight);
        newRect = new Rectangle(leftCorner, this.maxWidth + 40, 30);
        thisBlock = new Block(newRect, 0, java.awt.Color.BLACK);
        BallRemover deleter = new BallRemover(this, this.ballCounter);
        thisBlock.addHitListener(deleter);
        thisBlock.addToGame(this);

        // Right vertical wall
        leftCorner = new Point(this.maxWidth - 10, 30);
        newRect = new Rectangle(leftCorner, 10, this.maxHeight);
        thisBlock = new Block(newRect, 0, java.awt.Color.BLACK);
        thisBlock.addToGame(this);
    }

    /**
     * creates the balls and intializes their starting positions. The number of balls
     * is based on the info from levelinfo.
     */
    private void createBalls() {
        int startingXPos = (this.maxWidth / 2) - 3; 
        int startingYPos = this.maxHeight - 60;
        //gets the number of balls from the level
        for (int i = 0; i < this.levelInfo.numberOfBalls(); i++) {
            //creates balls
            Ball ball = new Ball(startingXPos, startingYPos, 6, java.awt.Color.BLACK, this.environment);
            //increases the number of balls in the counter
            this.ballCounter.increase(1);
            ball.setVelocity(this.getBallVelocities().get(i));
            ball.addToGame(this);
        }
    }
    
    private List<Velocity> getBallVelocities() {
        List<Velocity> returnList = new ArrayList<Velocity>();
        for (Velocity oneVeloc : this.levelInfo.initialBallVelocities()) {
            Velocity newVeloc = new Velocity(oneVeloc.getDeltaX(), oneVeloc.getDeltaY());
            returnList.add(newVeloc);
        }
        return returnList;
    }

    /**
     * implemented from the interface Animation. Does one frame, with drawing all the sprites
     * and checking for stoping the animation for the next frame.
     * @param d the drawsurface
     */
    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        //draws all the sprites
        this.sprites.drawAllOn(d);
        // The sprites then move one 'tick'
        this.sprites.notifyAllTimePassed(dt);

        //if the there are no balls left.
        if (this.ballCounter.getValue() <= 0) {
            //take off a life
            this.livesCounter.decrease(1);
            //stop the animation
            this.running = false;
        }

        //if the right number of blocks have been removed, clear the level
        if (this.destroyedEnoughBlocks()) {
            //add 100 points
            this.scoreCounter.increase(100);
            //stop the level from running
            this.running = false;
        }

        //if the user pauses the game, by pressing p, print the pause screen.
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard, KeyboardSensor.SPACE_KEY, new PauseScreen()));
        }
    }

    /**
     * implemented method from the interface Animation. checks if we should stop the animation
     * from running.
     * @return true or false.
     */
    @Override
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * static method of the maximal width.
     * @return MaxWidth
     */
    public static int maxWidth() {
        return 800;
    }

    /**
     * static method of the maximal height.
     * @return the max width
     */
    public static int maxHeight() {
        return 600;
    }

    /**
     * checks if we have destroyed enough blocks in order to move on to the next level.
     * @return true of false.
     */
    public boolean destroyedEnoughBlocks() {
        //checks if the right number of blocks have been removed
        return (this.totalBlocks - this.blockCounter.getValue()) >= this.levelInfo.numberOfBlocksToRemove();
    }

    /**
     * makes the colors and saves them in a list for later use.
     * @return List<java.awt.Color> list of colors
     */
    public static List<java.awt.Color> colorArray() {
        List<java.awt.Color> array = new ArrayList<java.awt.Color>();
        // Red
        array.add(new Color(255, 0, 0));
        // Orange
        array.add(new Color(255, 127, 0));
        // Yellow
        array.add(new Color(255, 255, 0));
        // Green
        array.add(new Color(0, 255, 0));
        // Blue
        array.add(new Color(0, 0, 255));
        // Indigo
        array.add(new Color(75, 0, 130));
        // Violet
        array.add(new Color(139, 0, 255));
        return array;
    }
}