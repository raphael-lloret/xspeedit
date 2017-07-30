package fr.xspeedit.packer;
/**
 * This is the current algorithm used by XspeedIt.
 */
public class BasicXspeedItPacker implements XspeedItPacker {

    @Override
    public String pack(String entry) {
        StringBuilder builder = new StringBuilder();
        int currentSize = 0;
        for (int i = 0; i < entry.length(); i++) {
            char c = entry.charAt(i);
            int value = c - '0';
            currentSize += value;
            if (currentSize > MAX_SIZE) {
                currentSize = value;
                builder.append(DELIMITER);
            }
            builder.append(c);
        }
        return builder.toString();
    }

}
