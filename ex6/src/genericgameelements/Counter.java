package genericgameelements;

/**
 * The Counter class keeps track of a number which may be added to, decreased and accessed.
 * @author Benjy Berkowicz
 * @author Atara Razin
 *
 */
public class Counter {
    private int currCount = 0;

    /**
     * We add a number to the current count.
     * @param number the amount to add
     */
    public void increase(int number) {
        this.currCount += number;
    }

    /**
     * We subtract a number from the current count.
     * @param number the amount to subtract
     */
    public void decrease(int number) {
        this.currCount -= number;
    }

    /**
     * Access the current count.
     * @return the current count
     */
    public int getValue() {
        return this.currCount;
    }
 }