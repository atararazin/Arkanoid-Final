package arkanoidelements;

import genericgameelements.Counter;
import genericgameelements.HitListener;
import shapes.Ball;
/**
 * this class removes a ball if a ball hits the bottom of the screen.
 * @author Atara Razin
 * @author Benjy Berkowicz
 * AKA Batara Berkorazin
 */
public class BallRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBalls;

    /**
     * construtor.
     * @param game the game
     * @param remainingBalls the remaining balls
     */
    public BallRemover(GameLevel game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    /**
     * This takes care of the event that a ball hits the bottom, the death area.
     * If this happens, we remove the ball from the game. Plus, we decrease the nubmer
     * of balls that we are keeping track of.
     * @param beingHit the block that was hit
     * @param hitter the Ball that is doing the hitting.
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(game);
        this.remainingBalls.decrease(1);
    }
}