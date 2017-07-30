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
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AbstractPackList)) return false;
        AbstractPackList other = (AbstractPackList) obj;
        if (other.size() != size()) return false;
        boolean[] founds = new boolean[size()];
        for (Pack pack : packs) {
            boolean found = false;
            for (int i = 0; i < other.packs.size(); i++) {
                if (founds[i]) continue;
                if (pack.equals(other.packs.get(i))) {
                    founds[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }
}
