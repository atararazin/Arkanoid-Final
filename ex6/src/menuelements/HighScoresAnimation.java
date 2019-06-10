package menuelements;

import java.util.List;

import arkanoidelements.GameLevel;
import biuoop.DrawSurface;
import genericgameelements.Animation;

public class HighScoresAnimation implements Animation {
    private HighScoresTable scores;
    private int scrollingX;
    
    public HighScoresAnimation(HighScoresTable scores) {
        this.scores = scores;
        this.scrollingX = GameLevel.maxHeight() / 3;
    }
    
    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        List<ScoreInfo> listOfScores = this.scores.getHighScores();

        int startingXVal = (d.getHeight() / 3) - 50;
        int startingYVal = 180;
        this.scrollingX = (this.scrollingX + 3) % GameLevel.maxWidth();
        
        d.setColor(java.awt.Color.BLACK);
        d.fillRectangle(0, 0, GameLevel.maxWidth(), GameLevel.maxHeight());
        
        d.setColor(GameLevel.colorArray().get(0));
        d.drawText(scrollingX, startingYVal - 120, "High Scores", 35);
        
        d.setColor(GameLevel.colorArray().get(1));
        d.drawText(startingXVal, startingYVal - 65, "Name", 35);
        d.drawText((startingXVal * 3), startingYVal - 65, "Score", 35);
        d.drawText(startingXVal - 20, startingYVal - 55, "_________________________", 35);
        
        
        d.setColor(GameLevel.colorArray().get(2));
        for (int i = 0; i < listOfScores.size(); i++) {
             d.setColor(GameLevel.colorArray().get(1));
             d.drawText(startingXVal, startingYVal + (i * 35), Integer.toString(i+1), 25);
             d.setColor(GameLevel.colorArray().get(2));
             d.drawText(startingXVal + 30, startingYVal + (i * 35), listOfScores.get(i).getName(), 25);
             String scoreVal = Integer.toString(listOfScores.get(i).getScore());
             d.drawText(startingXVal * 3, startingYVal + (i * 35), scoreVal, 25);
        }
    }

    @Override
    public boolean shouldStop() {
        return false;
    }
}