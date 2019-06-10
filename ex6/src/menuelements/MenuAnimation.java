package menuelements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.awt.Color;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

public class MenuAnimation<T> implements Menu<T> {

    private List<String> optionList;
    private List<String> keysList;
    private Map<String, Menu<T>> subMenuMap;
    private List<T> taskList;
    private String title;
    private int blue;
    private int mult;
    private boolean readyToStop;
    private T status;
    private KeyboardSensor keyboard;
    private String subMenuKey;

    public MenuAnimation(String title, KeyboardSensor keyboard) {
        this.optionList = new ArrayList<String>();
        this.taskList = new ArrayList<T>();
        this.keysList = new ArrayList<String>();
        this.blue = 100;
        this.readyToStop = false;
        this.keyboard = keyboard;
        this.title = title;
        this.mult = 1;
        this.subMenuMap = new TreeMap<String, Menu<T>>();
    }

    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        if (this.subMenuKey != null) {
            Menu<T> subMenu = this.subMenuMap.get(this.subMenuKey);
            subMenu.doOneFrame(d, dt);
            if (subMenu.shouldStop()) {
                this.status = subMenu.getStatus();
                this.readyToStop = true;
            }
        } else {
            d.setColor(Color.BLACK);
            d.fillRectangle(0, 0, d.getWidth(), d.getHeight());

            this.blue = (this.blue + (this.mult * 8));
            
            if (this.blue >= 255 || this.blue <= 0) {
                this.mult *= -1;
                this.blue = Math.max(0, Math.min(this.blue, 255));
            }

            int startingXVal = (d.getHeight() / 3) - 50;
            int startingYVal = 210;

            d.setColor(new Color(240, 255, blue));
            d.drawText(140, startingYVal - 120, this.title, 35);

            d.setColor(new Color(160, 32, 240));
            for (int i = 0; i < this.optionList.size(); i++) {
                String text = this.keysList.get(i) + ". " + this.optionList.get(i);
                d.drawText(startingXVal, startingYVal + (i * 35), text, 35);
            }

            for (int i = 0; i < this.optionList.size(); i++) {
                String checkKey = this.keysList.get(i);
                if (this.keyboard.isPressed(checkKey)) {
                    this.status = this.taskList.get(i);
                    if (this.status != null) {
                        this.readyToStop = true;
                    } else {
                        this.subMenuKey = checkKey;
                    }
                }
            }
        }

    }

    @Override
    public boolean shouldStop() {
        return this.readyToStop;
    }

    @Override
    public void addSelection(String key, String message, T returnVal) {
        this.keysList.add(key);
        this.optionList.add(message);
        this.taskList.add(returnVal);
    }

    @Override
    public T getStatus() {
        return this.status;
    }

    public void resetMenu() {
        this.readyToStop = false;
        this.subMenuKey = null;
    }

    public void updateSelection(String key, T returnVal) {
        int pos = this.keysList.lastIndexOf(key);
        this.taskList.add(pos, returnVal);
        this.taskList.remove(pos + 1);
    }
    
    public void updateSubMenu(String key, Menu<T> subMenu) {
        this.subMenuMap.put(key, subMenu);
    }

    @Override
    public void addSubMenu(String key, String message, final Menu<T> subMenu) {
        this.keysList.add(key);
        this.optionList.add(message);
        this.taskList.add(null);
        this.subMenuMap.put(key, subMenu);
    }

}
