package levelfilereaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import arkanoidelements.Block;

public class BlocksFromSymbolsFactory {
    private Map<String, Integer> spacerWidths;
    private Map<String, BlockCreator> blockCreators;

    public BlocksFromSymbolsFactory(Map<String, Integer> spacerWidths, Map<String, BlockCreator> blockCreators) {
        this.spacerWidths = spacerWidths;
        this.blockCreators = blockCreators;
    }

    public List<Block> symbolsListToBlocksList(List<String> symbolsList, int startX, int startY, int rowHeight) {
        List<Block> blocksArray = new ArrayList<Block>();
        int runningY = startY + 40;
        int runningX = startX;
        for (String blocksLine : symbolsList) {
            for (char symbol : blocksLine.toCharArray()) {
                String oneSymbol = Character.toString(symbol); 
                if (this.isSpaceSymbol(oneSymbol)) {
                    runningX += this.getSpaceWidth(oneSymbol);
                }
                else if (this.isBlockSymbol(oneSymbol)) {
                    Block newBlock = this.getBlock(oneSymbol, runningX, runningY);
                    blocksArray.add(newBlock);
                    runningX += newBlock.getCollisionRectangle().getWidth();
                }
            }
            runningY += rowHeight;
            runningX = startX;
        }
        return blocksArray;
    }
    
    // returns true if 's' is a valid space symbol.
    public boolean isSpaceSymbol(String s) {
        return spacerWidths.containsKey(s);
    }

    // returns true if 's' is a valid block symbol.
    public boolean isBlockSymbol(String s) {
        return blockCreators.containsKey(s);
    }

    // Return a block according to the definitions associated
    // with symbol s. The block will be located at position (xpos, ypos).
    public Block getBlock(String s, int x, int y) {
        return this.blockCreators.get(s).create(x, y);
    }

    // Returns the width in pixels associated with the given spacer-symbol.
    public int getSpaceWidth(String s) {
        return this.spacerWidths.get(s);
    }
}
