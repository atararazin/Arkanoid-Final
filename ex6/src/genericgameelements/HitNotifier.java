package genericgameelements;

/**
 * The HitNotifier interface provides functionality for a collidable to pass out information of its 'hit'
 * status.
 * @author Benjy Berkowicz
 * @author Atara Razin
 *
 */
public interface HitNotifier {

    /**
     * Add a new hit-listener to the list.
     * @param hl a new hit listener
     */
    void addHitListener(HitListener hl);

    /**
     * Remove a given hit listener from the list.
     * @param hl a listener to be removed
     */
    void removeHitListener(HitListener hl);
 }
