package menuelements;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoresTable {

    private ArrayList<ScoreInfo> highscores;
    private int maxSize;

    // Create an empty high-scores table with the specified size.
    // The size means that the table holds up to size top scores.
    public HighScoresTable(int maximalSize) {
        this.highscores = new ArrayList<ScoreInfo>();
        this.maxSize = maximalSize;
    }

    // Add a high-score.
    public void add(ScoreInfo score) {
        this.highscores.add(score);
        Collections.sort(this.highscores);
        while (this.highscores.size() > this.maxSize) {
            this.highscores.remove(this.highscores.size() - 1);
        }
        
    }

    // Return table size.
    public int size() {
        return this.maxSize;
    }

    // Return the current high scores.
    // The list is sorted such that the highest
    // scores come first.
    public List<ScoreInfo> getHighScores() {
        return this.highscores;
    }

    // return the rank of the current score: where will it
    // be on the list if added?
    // Rank 1 means the score will be highest on the list.
    // Rank `size` means the score will be lowest.
    // Rank > `size` means the score is too low and will not
    // be added to the list.
    public int getRank(int checkScore) {
        int countTo = Math.min(this.size(), this.highscores.size());
        int i;
        for (i = 0; i < countTo; i++) {
            ScoreInfo nextScore = this.highscores.get(i);
            if (checkScore > nextScore.getScore()) {
                break;
            }
        }
        return i + 1;
    }

    // Clears the table
    public void clear() {
        this.highscores.clear();
    }

    // Load table data from file.
    // Current table data is cleared.
    public void load(File filename) throws IOException {
        ObjectInputStream objectReader = null;
        FileInputStream fileReader = null;
        this.clear();

        try {
            fileReader = new FileInputStream(filename);
            objectReader = new ObjectInputStream(fileReader);

            Integer numberOfElements = (Integer) objectReader.readObject();
            Integer maxSize = (Integer) objectReader.readObject();

            for (int i = 0; i < numberOfElements.intValue(); i++) {
                ScoreInfo nextScore = (ScoreInfo) objectReader.readObject();
                this.highscores.add(nextScore);
                if (i > maxSize.intValue()) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            // Can't find file to open
            System.err.println("Unable to find file: " + filename);
        } catch (ClassNotFoundException e) {
            // The class in the stream is unknown to the JVM
            System.err.println("Unable to find class for object in file: " + filename);
        } catch (IOException e) {
            // Some other problem
            System.err.println("Failed reading object");
            e.printStackTrace(System.err);
            throw e;
        } finally {
            try {
                if (objectReader != null) {
                    objectReader.close();
                }
            } catch (IOException e) {
                System.err.println("Failed closing file: " + filename);
                throw e;
            }
        }
    }

    // Save table data to the specified file.
    public void save(File filename) throws IOException {
        ObjectOutputStream objectWriter = null;
        FileOutputStream fileWriter = null;

        try {
            fileWriter = new FileOutputStream(filename);
            objectWriter = new ObjectOutputStream(fileWriter);

            // First we write how many scores we are about to write down
            objectWriter.writeObject(new Integer(this.highscores.size()));
            objectWriter.writeObject(new Integer(this.maxSize));

            // Now, we write all the scores one by one
            for (ScoreInfo nextScore : this.highscores) {
                objectWriter.writeObject(nextScore);
            }

        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (objectWriter != null) {
                    objectWriter.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
    }

    // Read a table from file and return it.
    // If the file does not exist, or there is a problem with
    // reading it, an empty table is returned.
    public static HighScoresTable loadFromFile(File filename, int defaultSize) {
        FileInputStream fileReader = null;
        HighScoresTable highscoresObject = new HighScoresTable(defaultSize);

        try {
            fileReader = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            fileReader = null;
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                    highscoresObject.load(filename);
                }
            } catch (IOException e) {
                System.err.println("Failed closing file: " + filename);
            }
        }
        return highscoresObject;
    }
}