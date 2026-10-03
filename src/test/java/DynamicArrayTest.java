import dataStructures.DynamicArray;
import metrics.Metrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicArrayTest {
    private DynamicArray array;
    private Random random;

    @BeforeEach
    void setUp() {
        array = new DynamicArray(new Metrics());
        random = new Random(42);
    }

    @Test
    @DisplayName("DynamicArray: Correctness on 100 random datasets")
    void testCorrectness() {
        for (int iteration = 0; iteration < 100; iteration++) {
            array = new DynamicArray(new Metrics());
            List<Integer> expected = new ArrayList<>();
            int size = random.nextInt(1000) + 10;

            for (int i = 0; i < size; i++) {
                int value = random.nextInt(10000);
                array.add(value);
                expected.add(value);
            }

            assertContents(expected);
            for (int i = 0; i < 100; i++) {
                int query = random.nextInt(12000);
                assertEquals(expected.contains(query), array.contains(query),
                        "Search error on iteration: " + iteration);
            }
        }
    }

    @Test
    @DisplayName("DynamicArray: 1000 mixed operations compared with ArrayList")
    void testMixedOperations() {
        List<Integer> expected = new ArrayList<>();

        for (int step = 0; step < 1000; step++) {
            int operation = random.nextInt(5);
            int value = random.nextInt(201) - 100;

            if (operation == 0) {
                array.add(value);
                expected.add(value);
            } else if (operation == 1 || expected.isEmpty()) {
                int index = random.nextInt(expected.size() + 1);
                array.add(index, value);
                expected.add(index, value);
            } else if (operation == 2) {
                int index = random.nextInt(expected.size());
                array.remove(index);
                expected.remove(index);
            } else if (operation == 3) {
                int index = random.nextInt(expected.size());
                assertEquals(expected.get(index).intValue(), array.get(index));
            } else {
                assertEquals(expected.contains(value), array.contains(value));
            }

            assertContents(expected);
        }
    }

    @Test
    @DisplayName("DynamicArray: Empty structure")
    void testEmptyStructure() {
        assertEquals(0, array.getSize());
        assertFalse(array.contains(10));
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(1, 10));

        array.add(0, 10);
        assertContents(List.of(10));
    }

    @Test
    @DisplayName("DynamicArray: One element and reuse after removal")
    void testSingleElement() {
        array.add(42);
        assertContents(List.of(42));
        assertTrue(array.contains(42));
        assertFalse(array.contains(43));

        array.remove(0);
        assertContents(List.of());
        assertFalse(array.contains(42));

        array.add(7);
        assertContents(List.of(7));
    }

    @Test
    @DisplayName("DynamicArray: Duplicate values")
    void testDuplicates() {
        for (int i = 0; i < 5; i++) {
            array.add(5);
        }

        array.remove(2);
        assertContents(List.of(5, 5, 5, 5));
        assertTrue(array.contains(5));

        for (int i = 0; i < 4; i++) {
            array.remove(0);
        }
        assertContents(List.of());
        assertFalse(array.contains(5));
    }

    @Test
    @DisplayName("DynamicArray: First, middle and last indices")
    void testBoundaryIndices() {
        array.add(20);
        array.add(40);
        array.add(0, 10);
        array.add(2, 30);
        array.add(4, 50);
        assertContents(List.of(10, 20, 30, 40, 50));
        assertEquals(10, array.get(0));
        assertEquals(50, array.get(4));

        array.remove(0);
        array.remove(3);
        array.remove(1);
        assertContents(List.of(20, 40));
    }

    @Test
    @DisplayName("DynamicArray: Invalid indices preserve contents")
    void testInvalidInput() {
        array.add(10);
        array.add(20);

        int[] invalidReadIndices = {-1, 2, 100, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int index : invalidReadIndices) {
            assertThrows(IndexOutOfBoundsException.class, () -> array.get(index));
            assertThrows(IndexOutOfBoundsException.class, () -> array.remove(index));
        }

        int[] invalidInsertIndices = {-1, 3, 100, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int index : invalidInsertIndices) {
            assertThrows(IndexOutOfBoundsException.class, () -> array.add(index, 30));
        }
        assertContents(List.of(10, 20));
    }

    @Test
    @DisplayName("DynamicArray: Growth preserves elements for both add methods")
    void testGrowth() {
        for (boolean insertAtHead : new boolean[]{false, true}) {
            array = new DynamicArray(new Metrics());
            List<Integer> expected = new ArrayList<>();

            for (int i = 0; i < 1000; i++) {
                if (insertAtHead) {
                    array.add(0, i);
                    expected.add(0, i);
                } else {
                    array.add(i);
                    expected.add(i);
                }
                assertContents(expected);
            }
        }
    }

    @Test
    @DisplayName("DynamicArray: Negative values and int limits")
    void testExtremeValues() {
        array.add(Integer.MIN_VALUE);
        array.add(-1);
        array.add(0);
        array.add(Integer.MAX_VALUE);
        assertContents(List.of(Integer.MIN_VALUE, -1, 0, Integer.MAX_VALUE));
        assertTrue(array.contains(Integer.MIN_VALUE));
        assertTrue(array.contains(Integer.MAX_VALUE));
        assertFalse(array.contains(1));
    }

    private void assertContents(List<Integer> expected) {
        assertEquals(expected.size(), array.getSize(), "Invalid size");
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).intValue(), array.get(i), "Invalid value at index: " + i);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(expected.size()));
    }
}
