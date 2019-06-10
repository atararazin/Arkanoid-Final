package levelfilereaders;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import arkanoidelements.Block;
import genericgameelements.Sprite;
import shapes.Point;
import shapes.Rectangle;

public class BlocksDefinitionReader {
    public static BlocksFromSymbolsFactory fromReader(java.io.Reader reader) {
        BufferedReader buffy = new BufferedReader(reader);
        Map<String, Integer> spacerWidths = new TreeMap<String, Integer>();
        Map<String, BlockCreator> blockCreators = new TreeMap<String, BlockCreator>();
        Map<String, String> defaultList = new TreeMap<String, String>();
        Map<String, String> oneBlockDef = new TreeMap<String, String>();

        
        String[] requiredBlockList = { "symbol", "height", "width", "hit_points" };
        String[] requiredSpacerList = { "symbol", "width" };

        while (true) {
            try {
                String readLine = buffy.readLine();
                if (readLine == null) {
                    break;
                } else if (readLine.length() <= 1 || readLine.startsWith("#")) {
                    continue;
                } else if (readLine.startsWith("default")) {
                    Pattern regex = Pattern.compile("(\\S+):(\\S+)");
                    Matcher matcher = regex.matcher(readLine);
                    while (matcher.find()) {
                        defaultList.put(matcher.group(1), matcher.group(2));
                    }
                } else if (readLine.startsWith("bdef")) {
                    oneBlockDef.clear();
                    // first we copy all default values into the block creator
                    oneBlockDef.putAll(defaultList);
                    
                    Pattern regex = Pattern.compile("(\\S+):(\\S+)");
                    Matcher matcher = regex.matcher(readLine);
                    while (matcher.find()) {
                        oneBlockDef.put(matcher.group(1), matcher.group(2));
                    }

                    // if we are missing any required parameters, we throw an
                    // error
                    for (String check : requiredBlockList) {
                        if (!oneBlockDef.containsKey(check)) {
                            System.err.println("Invalid block file format, missing: " + check);
                            return null;
                        }
                    }

                    String symbol = oneBlockDef.get("symbol");
                    final int width = Integer.parseInt(oneBlockDef.get("width"));
                    final int height = Integer.parseInt(oneBlockDef.get("height"));
                    final int hitpoints = Integer.parseInt(oneBlockDef.get("hit_points"));
                    final Color stroke;
                    if (oneBlockDef.get("stroke") != null) {
                        String strokeColor = extractBrackets(oneBlockDef.get("stroke"));
                        stroke = ColorParser.colorFromString(strokeColor);
                    } else {
                        stroke = null;
                    }

                    final Map<Integer, Image> imageMap = new TreeMap<Integer, Image>();
                    final Map<Integer, Color> colorMap = new TreeMap<Integer, Color>();

                    for (int i = hitpoints; i >= 0; i--) {
                        String search;
                        if (i != 0) {
                            search = "fill-" + Integer.toString(i);
                        } else {
                            search = "fill";
                        }

                        if (oneBlockDef.containsKey(search)) {
                            if (oneBlockDef.get(search).startsWith("image")) {
                                String file = extractBrackets(oneBlockDef.get(search));
                                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(file);
                                Image img = null;
                                try {
                                    img = ImageIO.read(is);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (i != 0) {
                                    imageMap.put(i, img);
                                } else {
                                    imageMap.put(-1, img);
                                }
                            }
                            // Otherwise the background is a color
                            else {
                                String colorString = extractBrackets(oneBlockDef.get(search));
                                Color color = ColorParser.colorFromString(colorString);
                                if (i != 0) {
                                    colorMap.put(i, color);
                                } else {
                                    colorMap.put(-1, color);
                                }
                            }

                        }
                    }

                    BlockCreator bMaker = new BlockCreator() {
                        public Block create(int x, int y) {
                            Map<Integer, Sprite> hitBackgrounds = new TreeMap<Integer, Sprite>();
                            for (int index : imageMap.keySet()) {
                                Sprite sprite = SpriteMaker.fromImage(imageMap.get(index), x + 1, y + 1);
                                hitBackgrounds.put(index, sprite);
                            }
                            for (int index : colorMap.keySet()) {
                                Sprite sprite = SpriteMaker.fromColor(colorMap.get(index), x + 1, y + 1, width, height);
                                hitBackgrounds.put(index, sprite);
                            }
                            final Rectangle rect = new Rectangle(new Point((double) x, (double) y), width, height);
                            return new Block(rect, hitpoints, hitBackgrounds, stroke);
                        }
                    };
                    // We have now assembled one block maker associated with a
                    // given symbol. We place it in the list.
                    blockCreators.put(symbol, bMaker);

                } else if (readLine.startsWith("sdef")) {
                    Map<String, String> oneSpaceDef = new TreeMap<String, String>();
                    Pattern regex = Pattern.compile("(\\S+):(\\S+)");
                    Matcher matcher = regex.matcher(readLine);
                    while (matcher.find()) {
                        oneSpaceDef.put(matcher.group(1), matcher.group(2));
                    }

                    for (String check : requiredSpacerList) {
                        if (!oneSpaceDef.containsKey(check)) {
                            System.err.println("Invalid spacer file format, missing: " + check);
                            return null;
                        }
                    }

                    spacerWidths.put(oneSpaceDef.get("symbol"), Integer.parseInt(oneSpaceDef.get("width")));
                }
            } catch (EOFException e) {
                break;
            } catch (IOException a) {
                System.err.println("Error closing file!");
            }
        }

        BlocksFromSymbolsFactory blockMaker = new BlocksFromSymbolsFactory(spacerWidths, blockCreators);

        return blockMaker;
    }

    public static String extractBrackets(String withBrackets) {
        Pattern p = Pattern.compile("\\(([^\\)]+)\\)");
        Matcher matcher = p.matcher(withBrackets);
        matcher.find();
        return matcher.group(1);
    }
}
