import dataStructures.MyLinkedList;
import metrics.Metrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {
    private MyLinkedList list;
    private Random random;

    @BeforeEach
    void setUp() {
        list = new MyLinkedList(new Metrics());
        random = new Random(42);
    }

    @Test
    @DisplayName("MyLinkedList: Correctness on 100 random datasets")
    void testCorrectness() {
        for (int iteration = 0; iteration < 100; iteration++) {
            list = new MyLinkedList(new Metrics());
            List<Integer> expected = new LinkedList<>();
            int size = random.nextInt(1000) + 10;

            for (int i = 0; i < size; i++) {
                int value = random.nextInt(10000);
                list.add(value);
                expected.add(value);
            }

            assertContents(expected);
            for (int i = 0; i < 100; i++) {
                int query = random.nextInt(12000);
                assertEquals(expected.contains(query), list.contains(query),
                        "Search error on iteration: " + iteration);
            }
        }
    }

    @Test
    @DisplayName("MyLinkedList: 1000 mixed operations compared with LinkedList")
    void testMixedOperations() {
        List<Integer> expected = new LinkedList<>();

        for (int step = 0; step < 1000; step++) {
            int operation = random.nextInt(5);
            int value = random.nextInt(201) - 100;

            if (operation == 0) {
                list.add(value);
                expected.add(value);
            } else if (operation == 1 || expected.isEmpty()) {
                int index = random.nextInt(expected.size() + 1);
                list.add(index, value);
                expected.add(index, value);
            } else if (operation == 2) {
                int index = random.nextInt(expected.size());
                list.remove(index);
                expected.remove(index);
            } else if (operation == 3) {
                int index = random.nextInt(expected.size());
                assertEquals(expected.get(index).intValue(), list.get(index));
            } else {
                assertEquals(expected.contains(value), list.contains(value));
            }

            assertContents(expected);
        }
    }

    @Test
    @DisplayName("MyLinkedList: Empty structure")
    void testEmptyStructure() {
        assertFalse(list.contains(10));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, 10));

        list.add(0, 10);
        assertContents(List.of(10));
    }

    @Test
    @DisplayName("MyLinkedList: One element and reuse after removal")
    void testSingleElement() {
        list.add(42);
        assertContents(List.of(42));
        assertTrue(list.contains(42));
        assertFalse(list.contains(43));

        list.remove(0);
        assertContents(List.of());
        assertFalse(list.contains(42));

        list.add(0, 7);
        list.add(8);
        assertContents(List.of(7, 8));
    }

    @Test
    @DisplayName("MyLinkedList: Duplicate values")
    void testDuplicates() {
        for (int i = 0; i < 5; i++) {
            list.add(5);
        }

        list.remove(2);
        assertContents(List.of(5, 5, 5, 5));
        assertTrue(list.contains(5));

        for (int i = 0; i < 4; i++) {
            list.remove(0);
        }
        assertContents(List.of());
        assertFalse(list.contains(5));
    }

    @Test
    @DisplayName("MyLinkedList: First, middle and last indices")
    void testBoundaryIndices() {
        list.add(20);
        list.add(40);
        list.add(0, 10);
        list.add(2, 30);
        list.add(4, 50);
        assertContents(List.of(10, 20, 30, 40, 50));
        assertEquals(10, list.get(0));
        assertEquals(50, list.get(4));

        list.remove(0);
        list.remove(3);
        list.remove(1);
        assertContents(List.of(20, 40));

        list.add(0, 10);
        list.add(3, 50);
        assertContents(List.of(10, 20, 40, 50));
    }

    @Test
    @DisplayName("MyLinkedList: Invalid indices preserve contents")
    void testInvalidInput() {
        list.add(10);
        list.add(20);

        int[] invalidReadIndices = {-1, 2, 100, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int index : invalidReadIndices) {
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
            assertThrows(IndexOutOfBoundsException.class, () -> list.remove(index));
        }

        int[] invalidInsertIndices = {-1, 3, 100, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int index : invalidInsertIndices) {
            assertThrows(IndexOutOfBoundsException.class, () -> list.add(index, 30));
        }
        assertContents(List.of(10, 20));
    }

    @Test
    @DisplayName("MyLinkedList: Negative values and int limits")
    void testExtremeValues() {
        list.add(Integer.MIN_VALUE);
        list.add(-1);
        list.add(0);
        list.add(Integer.MAX_VALUE);
        assertContents(List.of(Integer.MIN_VALUE, -1, 0, Integer.MAX_VALUE));
        assertTrue(list.contains(Integer.MIN_VALUE));
        assertTrue(list.contains(Integer.MAX_VALUE));
        assertFalse(list.contains(1));
    }

    private void assertContents(List<Integer> expected) {
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).intValue(), list.get(i), "Invalid value at index: " + i);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(expected.size()));
    }
}
