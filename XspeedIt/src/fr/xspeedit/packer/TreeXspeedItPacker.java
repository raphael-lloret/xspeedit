package fr.xspeedit.packer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Find an optimal package by trying all possible pack combination. this work by creating a tree with all possibilities
 * and looking for the final possibilities with the smallest amount of pack.
 * 
 * <pre>
 * Here is an example with the following entry : 248.
 * ()->2
 *  |  |->24
 *  |  |  |->24/8
 *  |  |
 *  |  |->2/4
 *  |  |  |->2/4/8
 *  |  |
 *  |  |->28
 *  |  |  |->28/4
 *  |  |
 *  |  |->2/8
 *  |     |->2/8/4
 *  |
 *  |->4
 *  |  |->42
 *  |  |  |->42/8
 *  |  |
 *  |  |->4/2
 *  |  |  |->4/28
 *  |  |  |->4/2
 *  |  |
 *  |  |->4/8
 *  |  |  |->4/8/2
 *  |  |  |->4/82
 *  |
 *  |->8
 *     |->8/4
 *     |  |->8/4/2
 *     |  |->8/4
 *     |
 *     |->82
 *     |  |->82/4
 *     |
 *     |->8/2
 *        |->8/2/4
 *        |->8/24
 * In this case the best solution is any of the solutions with two packets.
 * </pre>
 * 
 * The actual tree is not stored as a tree but as a collection of all non expanded node (called state in the algorithm).
 * In the previous example there is no need to store the node '82' when the node '82/4' have been computed.
 */
public class TreeXspeedItPacker implements XspeedItPacker {

    /**
     * A single state (node) of the tree.
     */
    protected static class State extends AbstractPackList {
        /**
         * List of items that haven't been placed in a pack at this state. unpackedValues[i] represent the number of
         * item of value (i+1) left.
         */
        final int[] unpackedValues;
        
        /** Number of item that have yet to be packet. */
        int         unpackedValuesCount = 0;

        private State(int[] unpackedValues, List<Pack> packs) {
            this.unpackedValues = unpackedValues;
            this.packs = packs;
        }

        /**
         * Create a default state where no item is packed.
         * 
         * @param entry
         *            List of item to pack.
         */
        public State(String entry) {
            unpackedValues = new int[9];
            entry.chars().map(i -> i - '1').forEach(value -> {
                unpackedValues[value]++;
                unpackedValuesCount++;
            });
        }

        /**
         * Create a list of all possible state computed from the current state. Will return an empty list if the state
         * is final.
         */
        public List<State> createSubState() {
            List<State> substates = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                if (unpackedValues[i] == 0)
                    continue;
                int[] subUnpackedValues = Arrays.copyOf(unpackedValues, unpackedValues.length);
                subUnpackedValues[i]--;
                substates.addAll(createSubState(i + 1, subUnpackedValues));
            }
            return substates;
        }

        /**
         * Check if this is a final state.
         */
        public boolean isFinal() {
            return unpackedValuesCount == 0;
        }

        private List<State> createSubState(int value, int[] subUnpackedValues) {
            List<State> subStates = new ArrayList<>();
            for (int i = 0; i < packs.size(); i++) {
                if (packs.get(i).canAccept(value)) {
                    List<Pack> clones = clonePacks();
                    clones.get(i).add(value);
                    State subState = new State(subUnpackedValues, clones);
                    subState.unpackedValuesCount = unpackedValuesCount - 1;
                    subStates.add(subState);
                }
            }

            List<Pack> clones = clonePacks();
            Pack pack = new Pack();
            pack.add(value);
            clones.add(pack);
            State subState = new State(subUnpackedValues, clones);
            subState.unpackedValuesCount = unpackedValuesCount - 1;
            subStates.add(subState);

            return subStates;
        }

        private List<Pack> clonePacks() {
            List<Pack> clones = new ArrayList<>(packs.size());
            for (Pack pack : packs) {
                clones.add(pack.clonePack());
            }
            return clones;
        }
    }

    @Override
    public String pack(String entry) {
        State defaultState = optimise(new State(entry));
        if (defaultState.isFinal()) {
            return defaultState.toString();
        }
        // The order is needed to limit the size of the queue. smaller queue == less memory manipulation -> massive
        // increase in speed !
        PriorityQueue<State> queue = new PriorityQueue<>((s1, s2) -> s2.size() - s1.size());
        queue.add(defaultState);
        State bestState = null;
        int bestCost = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            final State state = queue.poll();
            List<State> subStates = state.createSubState();
            for (State sub : subStates) {
                if (sub.isFinal()) {
                    if (sub.size() < bestCost) {
                        bestState = sub;
                        bestCost = sub.size();
                        // All state with an higher cost than the current best will always be useless, they can be removed safely.
                        // This create a significant gain in speed especially the original entry contain a lot of small values.
                        queue.removeIf(s -> s.size() >= sub.size());
                    }
                } else {
                    queue.add(sub);
                }
            }
            // }
        }
        return bestState.toString();
    }

    /**
     * Try to optimize the original state.
     */
    protected State optimise(State state) {
        // No optimization in this case.
        return state;
    }

}
