package genericgameelements;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

/**
 * A KeyPressStoppableAnimation is an animation decorator which takes another
 * animation and allows it to stop when a key is pressed.
 * 
 * @author Benjy Berkowicz
 * @author Atara Razin
 *
 */
public class KeyPressStoppableAnimation implements Animation {

    private Animation internalAnimation;
    private KeyboardSensor keyboard;
    private String endKey;
    private boolean keyPressed;
    private boolean isAlreadyPressed;

    /**
     * The constructor requires an end key and a keyboard sensor.
     * 
     * @param keyboard
     *            the keyboard sensor
     * @param endKey
     *            the stop key
     * @param internalAnimation
     *            the animation to be decorated
     */
    public KeyPressStoppableAnimation(KeyboardSensor keyboard, String endKey, Animation internalAnimation) {
        this.internalAnimation = internalAnimation;
        this.keyboard = keyboard;
        this.endKey = endKey;
        this.keyPressed = false;
        this.isAlreadyPressed = true;
    }

    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        internalAnimation.doOneFrame(d, dt);
        if (keyboard.isPressed(endKey)) {
            if (!this.isAlreadyPressed) {
                this.keyPressed = true;
            }
        } else {
            this.isAlreadyPressed = false;
        }
    }

    @Override
    public boolean shouldStop() {
        return this.keyPressed;
    }

    /**
     * prepares the animation for pressing again.
     */
    public void reset() {
        this.keyPressed = false;
        this.isAlreadyPressed = true;
    }
}