package levelfilereaders;

import java.awt.Color;
import java.awt.Image;

import arkanoidelements.GameLevel;
import biuoop.DrawSurface;
import genericgameelements.Sprite;

public class SpriteMaker {
    public static Sprite fromColor(final Color c, final int x, final int y, final int width, final int height) {
        Sprite s = new Sprite() {
            public void drawOn(DrawSurface d) {
                d.setColor(c);
                d.fillRectangle(x, y, width, height);
            }
            
            public void timePassed(double dt) {
                
            }

            public void addToGame(GameLevel g) {
                g.addSprite(this);
            }
        };
        return s;
    }
    
    public static Sprite fromImage(final Image img, final int x, final int y) {
        Sprite s = new Sprite() {

            public void drawOn(DrawSurface d) {
                d.drawImage(x, y, img);
            }
            
            public void timePassed(double dt) {
                
            }

            public void addToGame(GameLevel g) {
                g.addSprite(this);
            }
        };
        return s;
    }
}
