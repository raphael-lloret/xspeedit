package fr.xspeedit.packer;

import java.util.ArrayList;
import java.util.List;

/**
 * REpresent a single packet.
 */
class Pack implements Cloneable {
    /** list used to store the items. */
    private final List<Integer> items      = new ArrayList<>();

    /** Current value of the packet. Must be equal to the sum of all the items in {@link #items}. */
    private int                 currentValue = 0;

    /**
     * Check if an item can be added to this pack.
     * 
     * @param value
     *            The item to check.
     * @return <code>true</code> if the item can be added, <code>false</code> otherwise.
     */
    public boolean canAccept(int value) {
        return currentValue + value <= XspeedItPacker.MAX_SIZE;
    }

    /**
     * Add the item to the packet. This method doesn't check if the pack can contain the item without going over the
     * limit. use {@link #canAccept(int)} to ensure that this operation is correct.
     * 
     * @param value
     *            Item to add.
     */
    public void add(int value) {
        currentValue += value;
        // The values are sorted to simplify the equals used by some algorithms.
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) <= value) {
                items.add(i, value);
                return;
            }
        }
        items.add(value);
    }

    /**
     * @return The current weight of the packet.
     */
    public int weight() {
        return currentValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        items.forEach(i -> builder.append(i));
        return builder.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return clonePack();
    }

    /**
     * Clone this pack.
     * 
     * @return A new identical {@link Pack}.
     */
    public Pack clonePack() {
        Pack clone = new Pack();
        clone.currentValue = currentValue;
        clone.items.addAll(items);
        return clone;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Pack)) return false;
        Pack other = (Pack) obj;
        return items.equals(other.items);
    }
}