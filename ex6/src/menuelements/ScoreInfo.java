package menuelements;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ScoreInfo implements Comparable<ScoreInfo>, Serializable {
    String name;
    int score;
    
    public ScoreInfo(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public String getName() {
        return this.name;
    }
    public int getScore() { 
        return this.score;
    }
    
    @Override
    public int compareTo(ScoreInfo second) {
        return new Integer(this.getScore()).compareTo(new Integer(second.getScore())) * -1;
    }
 }