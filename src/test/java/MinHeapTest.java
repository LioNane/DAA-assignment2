import dataStructures.MinHeap;
import metrics.Metrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {
    private MinHeap heap;
    private Random random;
    private Field dataField;
    private Field sizeField;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        heap = new MinHeap(new Metrics());
        random = new Random(42);

        dataField = MinHeap.class.getDeclaredField("data");
        sizeField = MinHeap.class.getDeclaredField("size");
        dataField.setAccessible(true);
        sizeField.setAccessible(true);
    }

    @Test
    @DisplayName("MinHeap: Correctness and heap property on 100 random datasets")
    void testCorrectness() throws IllegalAccessException {
        for (int iteration = 0; iteration < 100; iteration++) {
            heap = new MinHeap(new Metrics());
            int[] expected = new int[random.nextInt(200) + 10];
            int minimum = Integer.MAX_VALUE;

            for (int i = 0; i < expected.length; i++) {
                expected[i] = random.nextInt(2001) - 1000;
                heap.insert(expected[i]);
                minimum = Math.min(minimum, expected[i]);
                assertEquals(minimum, heap.peekMin());
                assertHeapProperty();
            }

            Arrays.sort(expected);
            int[] actual = new int[expected.length];
            for (int i = 0; i < actual.length; i++) {
                assertEquals(expected[i], heap.peekMin());
                actual[i] = heap.extractMin();
                assertHeapProperty();
            }

            assertArrayEquals(expected, actual, "Extraction error on iteration: " + iteration);
            assertThrows(IllegalStateException.class, () -> heap.extractMin());
        }
    }

    @Test
    @DisplayName("MinHeap: 1000 mixed operations compared with PriorityQueue")
    void testMixedOperations() throws IllegalAccessException {
        PriorityQueue<Integer> expected = new PriorityQueue<>();

        for (int step = 0; step < 1000; step++) {
            if (expected.isEmpty() || random.nextInt(3) == 0) {
                int value = random.nextInt(201) - 100;
                heap.insert(value);
                expected.add(value);
                assertHeapProperty();
            } else if (random.nextBoolean()) {
                assertEquals(expected.remove().intValue(), heap.extractMin());
                assertHeapProperty();
            } else {
                assertEquals(expected.peek().intValue(), heap.peekMin());
            }

            assertEquals(expected.size(), sizeField.getInt(heap));
            if (expected.isEmpty()) {
                assertThrows(IllegalStateException.class, () -> heap.peekMin());
            } else {
                assertEquals(expected.peek().intValue(), heap.peekMin());
            }
        }

        while (!expected.isEmpty()) {
            assertEquals(expected.remove().intValue(), heap.extractMin());
            assertHeapProperty();
        }
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
    }

    @Test
    @DisplayName("MinHeap: Empty structure")
    void testEmptyStructure() throws IllegalAccessException {
        assertThrows(IllegalStateException.class, () -> heap.peekMin());
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
        assertHeapProperty();

        heap.insert(10);
        assertHeapProperty();
        assertEquals(10, heap.peekMin());
        assertEquals(10, heap.extractMin());
        assertHeapProperty();
    }

    @Test
    @DisplayName("MinHeap: One element, repeated peek and reuse")
    void testSingleElement() throws IllegalAccessException {
        heap.insert(42);
        assertHeapProperty();
        assertEquals(42, heap.peekMin());
        assertEquals(42, heap.peekMin());
        assertEquals(42, heap.extractMin());
        assertHeapProperty();
        assertThrows(IllegalStateException.class, () -> heap.peekMin());
        assertThrows(IllegalStateException.class, () -> heap.extractMin());

        heap.insert(7);
        assertHeapProperty();
        assertEquals(7, heap.extractMin());
        assertHeapProperty();
    }

    @Test
    @DisplayName("MinHeap: Duplicate values")
    void testDuplicates() throws IllegalAccessException {
        for (int i = 0; i < 20; i++) {
            heap.insert(5);
            assertHeapProperty();
        }

        for (int i = 0; i < 20; i++) {
            assertEquals(5, heap.peekMin());
            assertEquals(5, heap.extractMin());
            assertHeapProperty();
        }
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
    }

    @Test
    @DisplayName("MinHeap: Increasing and decreasing insertion order")
    void testOrderedInput() throws IllegalAccessException {
        for (int direction : new int[]{1, -1}) {
            heap = new MinHeap(new Metrics());
            for (int i = 0; i < 100; i++) {
                heap.insert(direction == 1 ? i : 99 - i);
                assertHeapProperty();
            }

            for (int i = 0; i < 100; i++) {
                assertEquals(i, heap.extractMin());
                assertHeapProperty();
            }
        }
    }

    @Test
    @DisplayName("MinHeap: Sorted output and growth with 1000 values")
    void testSortedOutput() throws IllegalAccessException {
        for (int i = 0; i < 1000; i++) {
            heap.insert(random.nextInt());
            assertHeapProperty();
        }

        int previous = Integer.MIN_VALUE;
        for (int i = 0; i < 1000; i++) {
            int current = heap.extractMin();
            assertTrue(previous <= current, "Extracted values are not sorted at index: " + i);
            previous = current;
            assertHeapProperty();
        }
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
    }

    @Test
    @DisplayName("MinHeap: Negative values and int limits")
    void testExtremeValues() throws IllegalAccessException {
        int[] values = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, -1, 1};
        for (int value : values) {
            heap.insert(value);
            assertHeapProperty();
        }

        Arrays.sort(values);
        for (int value : values) {
            assertEquals(value, heap.extractMin());
            assertHeapProperty();
        }
    }

    private void assertHeapProperty() throws IllegalAccessException {
        int[] data = (int[]) dataField.get(heap);
        int size = sizeField.getInt(heap);
        assertTrue(size >= 0 && size <= data.length, "Invalid heap size");

        for (int child = 1; child < size; child++) {
            int parent = (child - 1) / 2;
            assertTrue(data[parent] <= data[child],
                    "Heap property violated between indices " + parent + " and " + child);
        }
    }
}
