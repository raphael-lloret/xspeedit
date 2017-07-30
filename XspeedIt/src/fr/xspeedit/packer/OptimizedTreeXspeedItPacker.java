package fr.xspeedit.packer;

/**
 * An optimization of the {@link TreeXspeedItPacker} algorithm. Only work if XspeedItPacker.MAX_SIZE is 10. This work by
 * using the following points :
 * <ul>
 * <li>No two items of size 6 or above will be in the same packet. So a {@link Pack} can be created for each of such items.</li>
 * <li>For each of such packet we can immediately add an item of size <code>10 - packetSize</code> if such item is available.</li>
 * </ul>
 * Once the optimization is done use the regular {@link TreeXspeedItPacker} algorithm.
 */
public class OptimizedTreeXspeedItPacker extends TreeXspeedItPacker {

    @Override
    protected State optimise(State state) {
        for (int i = 8; i >= 5; i--) {
            int opposite = 8 - i;
            for (int j = 0; j < state.unpackedValues[i]; j++) {
                Pack pack = new Pack();
                pack.add(i + 1);
                if (state.unpackedValues[opposite] > 0) {
                    pack.add(opposite + 1);
                    state.unpackedValues[opposite]--;
                    state.unpackedValuesCount--;
                }
                state.packs.add(pack);
            }
            state.unpackedValuesCount -= state.unpackedValues[i];
            state.unpackedValues[i] = 0;
        }

        return state;
    }
}
