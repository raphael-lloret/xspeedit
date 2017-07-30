package fr.xspeedit.packer;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of packet.
 */
public abstract class AbstractPackList {
    protected List<Pack> packs = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < packs.size(); i++) {
            if (i != 0) {
                builder.append(XspeedItPacker.DELIMITER);
            }
            builder.append(packs.get(i));
        }
        return builder.toString();
    }

    public int size() {
        return packs.size();
    }
}
