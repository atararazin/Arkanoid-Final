package arkanoidelements;

import genericgameelements.Counter;
import genericgameelements.HitListener;
import shapes.Ball;

/**
 * A HitListener for the score. Increases points when blocks are hit.
 * @author Atara Razin
 * @author Benjy Berkowicz
 * AKA Batara Berkorazin
 *
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * constructor.
     * @param scoreCounter the Counter that keeps track of the score
     */
    public ScoreTrackingListener(Counter scoreCounter) {
       this.currentScore = scoreCounter;
    }

    /**
     * adds points to the score according to if the block was hit and if it was removed
     * from the game after that hit. If a block gets hit, add 5 points. If a block gets
     * removed, add 10 points.
     * @param beingHit the block that got hit
     * @param hitter the ball that hit it
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        //when a block gets hit, add 5 points
       currentScore.increase(5);
       //if a block gets removed, meaning its hit points are zero or less, then add 10 points
       if (beingHit.getHitPoints() <= 0) {
           currentScore.increase(10);
       }
    }
 }