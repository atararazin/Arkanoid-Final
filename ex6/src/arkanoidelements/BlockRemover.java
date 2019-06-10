package arkanoidelements;

import genericgameelements.Counter;
import genericgameelements.HitListener;
import shapes.Ball;

/**
 * This class removes a block if the block's hitpoints has reached zero or below.
 * @author Atara Razin
 * @author Benjy Berkowicz
 * AKA Batara Berkorazin
 *
 */
public class BlockRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBlocks;

    /**
     * constructor.
     * @param game the game
     * @param remainingBlocks the blocks that are left
     */
    public BlockRemover(GameLevel game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    /**
     * This checks if the hitPoints of the specific block is zero or less. if it is, the block
     * gets removed from the game and the number of remaining blocks gets decresed. It also
     * removes this listener from the block that is being removed from the game
     * @param beingHit the block that gets hit.
     * @param hitter the ball that hits that block.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        //ensures that the number of hit points has reached zero or below
        if (beingHit.getHitPoints() <= 0) {
            beingHit.removeFromGame(this.game);
            beingHit.removeHitListener(this);
            this.remainingBlocks.decrease(1);
        }
    }
 }
