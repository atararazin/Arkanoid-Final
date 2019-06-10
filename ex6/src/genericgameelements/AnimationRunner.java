package genericgameelements;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

/**
 * The AnimationRunner class takes a given animation and performs the calculations required
 * to display all frames at a given framerate, until a terminating condition is reached.
 * @author Benjy Berkowicz
 * @author Atara Razin
 *
 */
public class AnimationRunner {
    private GUI gui;
    private int framesPerSecond;
    private Sleeper sleeper;

    /**
     * The constructor.
     * @param gui the GUI to draw the animation on
     * @param sleeper the sleeper to perform the delay commands
     * @param framesPerSecond a set amount of frames to be displayed per second
     */
    public AnimationRunner(GUI gui, Sleeper sleeper, int framesPerSecond) {
        this.gui = gui;
        this.sleeper = sleeper;
        this.framesPerSecond = framesPerSecond;
    }

    /**
     * The main run function loops until the animation is supposed to stop displaying each frame.
     * @param animation the animation to be run.
     */
    public void run(Animation animation) {
       // The conversion between seconds and miliseconds is performed here for use in the sleeper.
       int millisecondsPerFrame = 1000 / this.framesPerSecond;
       // Loop until the animation tells us to stop
       while (!animation.shouldStop()) {
          long startTime = System.currentTimeMillis();
          DrawSurface d = gui.getDrawSurface();

          /* This is the amount of time all the objects are being told to move forward by (instead of one 'step'
           * of dx and dy)
           */
          double timeToSimulate = 1.0 / this.framesPerSecond;

          // The animation is aware of how to draw itself on a provided DrawSurface
          animation.doOneFrame(d, timeToSimulate);

          gui.show(d);
          long usedTime = System.currentTimeMillis() - startTime;
          long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
          if (milliSecondLeftToSleep > 0) {
              this.sleeper.sleepFor(milliSecondLeftToSleep);
          }
       }
    }
}