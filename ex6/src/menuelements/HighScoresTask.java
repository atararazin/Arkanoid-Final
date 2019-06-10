package menuelements;

import genericgameelements.Animation;
import genericgameelements.AnimationRunner;

public class HighScoresTask implements Task<Void> {
    private AnimationRunner runner;
    private Animation highScoresAnimation;
    
    public HighScoresTask(AnimationRunner runner, Animation highScoresAnimation) {
        this.runner = runner;
        this.highScoresAnimation = highScoresAnimation;
     }
     public Void run() {
        this.runner.run(this.highScoresAnimation);
        return null;
     }
}
