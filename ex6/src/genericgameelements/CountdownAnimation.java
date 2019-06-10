package genericgameelements;


import biuoop.DrawSurface;

/**
 * The CountdownAnimation will display the given gameScreen,
 * for numOfSeconds seconds, and on top of them it will show
 * a countdown from countFrom back to 1, where each number will
 * appear on the screen for (numOfSeconds / countFrom) seconds, before
 * it is replaced with the next one.
 * @author Benjy Berkowicz
 * @author Atara Razin
 *
 */
public class CountdownAnimation implements Animation {

    private double numOfSeconds;
    private int countFrom;
    private SpriteCollection gameScreen;
    private long startTime;
    private double displayTime;

    /**
     * The constructor requires the number of seconds for the countdown to take, the number to count from
     * and the sprites to display in the background.
     * @param numOfSeconds the amount of seconds total for the screen to last
     * @param countFrom the number from which we count down
     * @param gameScreen the sprites to be drawn.
     */
    public CountdownAnimation(double numOfSeconds, int countFrom, SpriteCollection gameScreen) {
        this.numOfSeconds = numOfSeconds;
        this.countFrom = countFrom;
        this.gameScreen = gameScreen;
        this.startTime = System.currentTimeMillis();
        this.displayTime = countFrom;
    }

    /**
     * This particular doOneFrame of the animation prints the countdown of remaining "time" as a fraction
     * of the amount of time left.
     * @param d the DrawSurface to draw on.
     * @param dt the time that has passed
     */
    public void doOneFrame(DrawSurface d, double dt) {
            // The amount of time in miliseconds in which each number is displayed.
            double waitTime = (this.numOfSeconds / this.countFrom) * 1000;

            // We draw all the sprites in the background.
            this.gameScreen.drawAllOn(d);

            // The text is displayed on top.
            d.setColor(java.awt.Color.PINK);
            String text = "Batara Berkorazin presents in... " + Integer.toString((int) this.displayTime);
            d.drawText(120, 300, text, 40);

            // The amount of milliseconds until the number is to be reduced is calculated here
            double milisecondsLeft = waitTime * ((this.countFrom - this.displayTime) + 1);
            if (Math.abs(System.currentTimeMillis() - this.startTime) > milisecondsLeft) {
                this.displayTime--;
            }
    }

    /**
     * The animation stops when the maximum amount of time has elapsed.
     * @return boolean, has the elapsed time passed
     */
    public boolean shouldStop() {
        return this.startTime + (this.numOfSeconds * 1000) < System.currentTimeMillis();
    }
}