package arkanoidelements;

import java.util.List;

import genericgameelements.Sprite;

/**
 * The LevelInformation interface provides all necessary data used to construct a given game level.
 * @author Benjy Berkowicz
 * @author Atara Razin
 *
 */
public interface LevelInformation {
    /**
     * The number of balls in the level.
     * @return the number of balls
     */
    int numberOfBalls();

    /**
     * The initial velocity of each ball. Note that initialBallVelocities().size() == numberOfBalls().
     * @return a list of the ball velocities.
     */
    List<Velocity> initialBallVelocities();

    /**
     * The speed at which the paddle should move.
     * @return the delta x for the paddle
     */
    int paddleSpeed();

    /**
     * The width of the paddle.
     * @return the width of the paddle.
     */
    int paddleWidth();

    /**
     * The level name will be displayed at the top of the screen.
     * @return the level name.
     */
    String levelName();

    /**
     * Returns a sprite with the background of the level.
     * @return the background sprite.
     */
    Sprite getBackground();

    /**
     * The Blocks that make up this level, each block contains
     * its size, color and location.
     * @return a list of all blocks to be drawn.
     */
    List<Block> blocks();

    /**
     * Number of levels that should be removed
     * before the level is considered to be "cleared".
     * This number should be <= blocks.size();
     * @return the amount of blocks required to end the game.
     */
    int numberOfBlocksToRemove();
 }