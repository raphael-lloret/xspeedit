package fr.xspeedit.packer;
import java.util.Comparator;

/**
 * Use the first-fit decreasing algorithm to pack the items. This is a fast method but it doesn't guaranty an optimal
 * result.
 */
public class FFDXspeedItPacker implements XspeedItPacker {

    /**
     * Simple pack list that add the new value in the first available pack.
     */
    private static class PackList extends AbstractPackList {

        /**
         * Add an item in the first available pack or create a new one.
         * 
         * @param value
         *            The item to add.
         */
        public void addToBucker(int value) {
            for (Pack pack : packs) {
                if (pack.canAccept(value)) {
                    pack.add(value);
                    return;
                }
            }
            Pack pack = new Pack();
            pack.add(value);
            packs.add(pack);
        }
    }

    @Override
    public String pack(String entry) {
        PackList list = new PackList();
        entry.chars().map(i -> i - '0').boxed().sorted(Comparator.reverseOrder())
                .forEach(value -> list.addToBucker(value));
        return list.toString();
    }

}
