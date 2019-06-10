package levelfilereaders;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import arkanoidelements.Block;
import arkanoidelements.GameLevel;
import arkanoidelements.LevelInformation;
import arkanoidelements.Velocity;
import genericgameelements.Sprite;

public class LevelSpecificationReader {
    public List<LevelInformation> fromReader(java.io.Reader reader) {
        List<LevelInformation> finalList = new ArrayList<LevelInformation>();
        BufferedReader buffy = new BufferedReader(reader);
        List<String> oneLevel = new ArrayList<String>();
        boolean addToList = false;

        while (true) {
            try {
                String readLine = buffy.readLine();
                if (readLine == null) {
                    break;
                }
                if (readLine.length() <= 1 || readLine.startsWith("#")) {
                    continue;
                } else if (readLine.startsWith("START_LEVEL")) {
                    addToList = true;
                } else if (readLine.startsWith("END_LEVEL")) {
                    addToList = false;
                    finalList.add(this.parseOneLevel(oneLevel));
                    oneLevel.clear();
                }

                if (addToList) {
                    oneLevel.add(readLine);
                }
            } catch (EOFException e) {
                break;
            } catch (IOException a) {
                System.err.println("Error closing file!");
            }
        }
        return finalList;
    }

    public LevelInformation parseOneLevel(List<String> fullLevel) {
        String levelName = null;
        Sprite background = null;
        List<Velocity> velocityList = new ArrayList<Velocity>();
        List<String> blockLayout = new ArrayList<String>();
        List<Block> blockList;
        BlocksFromSymbolsFactory blockFactory = null;
        Pattern regex;
        Matcher matcher;
        int paddleSpeed = 0;
        int paddleWidth = 0;
        int startingX = 0;
        int startingY = 0;
        int rowHeight = 0;
        int numberOfBlocks = 0;
        int findCount = 0;
        boolean amReadingBlocks = false;

        for (String oneLevelLine : fullLevel) {
            if (oneLevelLine.startsWith("level_name")) {
                regex = Pattern.compile(this.patterns().get(0));
                matcher = regex.matcher(oneLevelLine);
                matcher.find();
                levelName = matcher.group(1);
                findCount++;
            } else if (oneLevelLine.startsWith("ball_velocities")) {
                regex = Pattern.compile(this.patterns().get(1));
                matcher = regex.matcher(oneLevelLine);
                findCount++;
                while (matcher.find()) {
                    double angle = Double.parseDouble(matcher.group(2));
                    double speed = Double.parseDouble(matcher.group(3));
                    Velocity v = Velocity.fromAngleAndSpeed(angle, speed);
                    velocityList.add(v);
                }
            } else if (oneLevelLine.startsWith("background")) {
                regex = Pattern.compile(this.patterns().get(2));
                matcher = regex.matcher(oneLevelLine);
                matcher.find();
                findCount++;
                if (matcher.group(1).equals("color")) {
                    Color color = ColorParser.colorFromString(matcher.group(2));
                    background = SpriteMaker.fromColor(color, 0, 0, GameLevel.maxWidth(), GameLevel.maxHeight());
                } else {
                    InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(matcher.group(2));
                    Image img = null;
                    try {
                        img = ImageIO.read(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    background = SpriteMaker.fromImage(img, 0, 0);
                    if (img != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (oneLevelLine.startsWith("paddle_speed")) {
                regex = Pattern.compile(this.patterns().get(3));
                matcher = regex.matcher(oneLevelLine);
                matcher.find();
                findCount++;
                paddleSpeed = Integer.parseInt(matcher.group(1));
            } else if (oneLevelLine.startsWith("paddle_width")) {
                regex = Pattern.compile(this.patterns().get(4));
                matcher = regex.matcher(oneLevelLine);
                matcher.find();
                findCount++;
                paddleWidth = Integer.parseInt(matcher.group(1));
            } else if (oneLevelLine.startsWith("block_definitions")) {
                regex = Pattern.compile(this.patterns().get(5));
                matcher = regex.matcher(oneLevelLine);
                matcher.find();
                findCount++;
                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(matcher.group(1));
                Reader reader = new InputStreamReader(is);
                blockFactory = BlocksDefinitionReader.fromReader(reader);
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (oneLevelLine.startsWith("blocks_start_x")) {
                regex = Pattern.compile(this.patterns().get(6));
                matcher = regex.matcher(oneLevelLine);
                matcher.find();
                findCount++;
                startingX = Integer.parseInt(matcher.group(1));
            } else if (oneLevelLine.startsWith("blocks_start_y")) {
                regex = Pattern.compile(this.patterns().get(7));
                matcher = regex.matcher(oneLevelLine);
                matcher.find();
                findCount++;
                startingY = Integer.parseInt(matcher.group(1));
            }

            else if (oneLevelLine.startsWith("row_height")) {
                regex = Pattern.compile(this.patterns().get(8));
                matcher = regex.matcher(oneLevelLine);
                matcher.find();
                findCount++;
                rowHeight = Integer.parseInt(matcher.group(1));
            } else if (oneLevelLine.startsWith("num_blocks")) {
                regex = Pattern.compile(this.patterns().get(9));
                matcher = regex.matcher(oneLevelLine);
                matcher.find();
                findCount++;
                numberOfBlocks = Integer.parseInt(matcher.group(1));
            } else if (oneLevelLine.startsWith("START_BLOCKS")) {
                amReadingBlocks = true;
                findCount++;
            } else if (oneLevelLine.startsWith("END_BLOCKS")) {
                amReadingBlocks = false;
                findCount++;
            }
            if (amReadingBlocks) {
                blockLayout.add(oneLevelLine);
            }

            

        }
        
        if (findCount < 12) {
            System.err.println("Missing required parameter in level specification file! Only got " + findCount);
        }

        // now we convert the blockLayout into a valid list of blocks using our
        // created factory.
        blockList = blockFactory.symbolsListToBlocksList(blockLayout, startingX, startingY, rowHeight);
        LevelInformation returnLevel = LevelInformationFactory.fromParams(velocityList, paddleSpeed, paddleWidth,
                levelName, background, blockList, numberOfBlocks);
        return returnLevel;
    }

    private List<String> patterns() {
        List<String> allPatterns = new ArrayList<String>();
        allPatterns.add("^level_name:(.*)");
        allPatterns.add("((-?\\d+),(-?\\d+)\\b)+");
        allPatterns.add("^background:(color|image)\\((.*)\\)");
        allPatterns.add("^paddle_speed:(\\d+)");
        allPatterns.add("^paddle_width:(\\d+)");
        allPatterns.add("^block_definitions:(.*)");
        allPatterns.add("^blocks_start_x:(\\d+)");
        allPatterns.add("^blocks_start_y:(\\d+)");
        allPatterns.add("^row_height:(\\d+)");
        allPatterns.add("^num_blocks:(\\d+)");
        return allPatterns;
    }
}
