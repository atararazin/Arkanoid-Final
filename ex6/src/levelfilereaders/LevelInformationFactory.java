package levelfilereaders;

import java.util.List;

import arkanoidelements.Block;
import arkanoidelements.LevelInformation;
import arkanoidelements.Velocity;
import genericgameelements.Sprite;

public class LevelInformationFactory {
    public static LevelInformation fromParams(final List<Velocity> velocityList, final int paddleSpeed,
            final int paddleWidth, final String levelName, final Sprite background, final List<Block> blockList,
            final int numberOfBlocks) {

        LevelInformation oneLevel = new LevelInformation() {
            public int numberOfBalls() {
                return velocityList.size();
            }

            public List<Velocity> initialBallVelocities() {
                return velocityList;
            }

            public int paddleSpeed() {
                return paddleSpeed;
            }

            public int paddleWidth() {
                return paddleWidth;
            }

            public String levelName() {
                return levelName;
            }

            public Sprite getBackground() {
                return background;
            }

            public List<Block> blocks() {
                return blockList;
            }

            public int numberOfBlocksToRemove() {
                return numberOfBlocks;
            }
        };
        return oneLevel;
    }
}
