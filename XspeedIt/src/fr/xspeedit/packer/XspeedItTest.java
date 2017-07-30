package fr.xspeedit.packer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class XspeedItTest {

    public static final int                VALUES_COUNT   = 10;
    // Value too high make OptimizedTreeXspeedItPacker run way to slow.
    public static final int                VALUES_SIZE    = 17;
    public static final Collection<String> VALUES_TO_TEST = new ArrayList<>(VALUES_COUNT);

    static {
        Random random = new Random();
        for (int i = 0; i < VALUES_COUNT; i++) {
            int[] array = new int[VALUES_SIZE];
            for (int j = 0; j < array.length; j++) {
                array[j] = random.nextInt(9) + '1';
            }
            VALUES_TO_TEST.add(new String(array, 0, array.length));
        }
    }

    public static void main(String[] args) {
        // A first run is needed to deal with JIT optimization messing up the test times.
        test(new BasicXspeedItPacker());

        test(new BasicXspeedItPacker());
        test(new FFDXspeedItPacker());
        test(new OptimizedTreeXspeedItPacker());
        // The TreeXspeedItSorter is really slow, modify the test to use small entry value in order to test it.
        // test(new TreeXspeedItPacker());
    }

    public static void test(XspeedItPacker sorter) {
        long startTime = System.currentTimeMillis();
        String sorted = sorter.pack("163841689525773");
        System.out.println(cost(sorted) + " -> " + sorted);
        for (String value : VALUES_TO_TEST) {
            sorted = sorter.pack(value);
            System.out.println(cost(sorted) + " -> " + sorted);
        }
        System.out.println("Running time for " + sorter.getClass().getSimpleName() + ": "
                + (System.currentTimeMillis() - startTime) + "ms.");
    }

    public static int cost(String value) {
        return (int) value.chars().filter(c -> c == XspeedItPacker.DELIMITER).count() + 1;
    }

}
