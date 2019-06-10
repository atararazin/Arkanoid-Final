package menuelements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import arkanoidelements.GameFlow;
import arkanoidelements.LevelInformation;
import levelfilereaders.LevelSpecificationReader;

public class PlayGameTask implements Task<Void> {
    private List<LevelInformation> levels;
    private GameFlow game;

    public PlayGameTask(GameFlow game, String file) {
        InputStream is = null;
        Reader reader = null;

        is = ClassLoader.getSystemClassLoader().getResourceAsStream(file);
        reader = new InputStreamReader(is);

        LevelSpecificationReader level = new LevelSpecificationReader();
        List<LevelInformation> levelList = level.fromReader(reader);

        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.levels = levelList;
        this.game = game;

    }

    @Override
    public Void run() {
        // run the levels
        this.game.runLevels(this.levels);
        return null;
    }
}
