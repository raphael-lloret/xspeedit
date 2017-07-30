package fr.xspeedit.packer;

/**
 * REpresent a single packet.
 */
class Pack implements Cloneable {
    /** String builder used to store the items and create the string representation of the packet. */
    private final StringBuilder builder      = new StringBuilder();

    /** Current value of the packet. Must be equal to the sum of all the items in {@link #builder}. */
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
        builder.append(value);
    }

    /**
     * @return The current weight of the packet.
     */
    public int weight() {
        return currentValue;
    }

    @Override
    public String toString() {
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
        clone.builder.append(builder.toString());
        return clone;
    }
}