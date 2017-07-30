package fr.xspeedit.packer;

/**
 * Interface for the packing algorithm used by XspeedIt.
 */
public interface XspeedItPacker {
    /** Maximum size for on package. */
    public static int  MAX_SIZE  = 10;

    /** Delimiter used to separate value in the packed string */
    public static char DELIMITER = '/';

    /**
     * Pack the list of item passed in entry.
     * 
     * @param entry
     *            List of item to pack. Should only contain numbers.
     * @return A packed list of items.
     */
    public String pack(String entry);
}
