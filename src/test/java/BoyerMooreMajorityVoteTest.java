
import org.example.algorithms.BoyerMooreMajorityVote;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Comprehensive unit tests for Boyer-Moore Majority Vote Algorithm
 */
class BoyerMooreMajorityVoteTest {

    private BoyerMooreMajorityVote algorithm;

    @BeforeEach
    void setUp() {
        algorithm = new BoyerMooreMajorityVote();
    }

    // ============ Basic Functionality Tests ============

    @Test
    @DisplayName("Single element array")
    void testSingleElement() {
        int[] arr = {42};
        Integer result = algorithm.findMajority(arr);
        assertEquals(42, result);
    }

    @Test
    @DisplayName("Two identical elements")
    void testTwoIdenticalElements() {
        int[] arr = {5, 5};
        Integer result = algorithm.findMajority(arr);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Two different elements - no majority")
    void testTwoDifferentElements() {
        int[] arr = {1, 2};
        Integer result = algorithm.findMajority(arr);
        assertNull(result);
    }

    @Test
    @DisplayName("Clear majority element")
    void testClearMajority() {
        int[] arr = {3, 3, 4, 2, 4, 4, 2, 4, 4};
        Integer result = algorithm.findMajority(arr);
        assertEquals(4, result);
    }

    @Test
    @DisplayName("Majority at beginning")
    void testMajorityAtBeginning() {
        int[] arr = {7, 7, 7, 7, 3, 2, 1};
        Integer result = algorithm.findMajority(arr);
        assertEquals(7, result);
    }

    @Test
    @DisplayName("Majority at end")
    void testMajorityAtEnd() {
        int[] arr = {1, 2, 3, 5, 5, 5, 5};
        Integer result = algorithm.findMajority(arr);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("No majority element")
    void testNoMajority() {
        int[] arr = {1, 2, 3, 4, 5};
        Integer result = algorithm.findMajority(arr);
        assertNull(result);
    }

    @Test
    @DisplayName("All elements identical")
    void testAllIdentical() {
        int[] arr = {9, 9, 9, 9, 9};
        Integer result = algorithm.findMajority(arr);
        assertEquals(9, result);
    }

    // ============ Edge Cases ============

    @Test
    @DisplayName("Null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.findMajority(null);
        });
    }

    @Test
    @DisplayName("Empty array throws exception")
    void testEmptyArray() {
        int[] arr = {};
        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.findMajority(arr);
        });
    }

    @Test
    @DisplayName("Element appearing exactly n/2 times - not majority")
    void testExactlyHalf() {
        int[] arr = {1, 1, 2, 2};
        Integer result = algorithm.findMajority(arr);
        assertNull(result);
    }

    @Test
    @DisplayName("Element appearing n/2 + 1 times - is majority")
    void testJustOverHalf() {
        int[] arr = {1, 1, 1, 2, 2};
        Integer result = algorithm.findMajority(arr);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Negative numbers")
    void testNegativeNumbers() {
        int[] arr = {-1, -1, -1, 2, 3};
        Integer result = algorithm.findMajority(arr);
        assertEquals(-1, result);
    }

    @Test
    @DisplayName("Mix of positive and negative")
    void testMixedNumbers() {
        int[] arr = {-5, -5, -5, 10, 10};
        Integer result = algorithm.findMajority(arr);
        assertEquals(-5, result);
    }

    @Test
    @DisplayName("Zero as majority element")
    void testZeroMajority() {
        int[] arr = {0, 0, 0, 1, 2};
        Integer result = algorithm.findMajority(arr);
        assertEquals(0, result);
    }

    // ============ Large Arrays ============

    @Test
    @DisplayName("Large array with clear majority")
    void testLargeArrayWithMajority() {
        int[] arr = new int[10001];
        // Fill with majority element (5001 occurrences)
        for (int i = 0; i < 5001; i++) {
            arr[i] = 42;
        }
        // Fill rest with different elements
        for (int i = 5001; i < 10001; i++) {
            arr[i] = i;
        }

        Integer result = algorithm.findMajority(arr);
        assertEquals(42, result);
    }

    @Test
    @DisplayName("Large array with no majority")
    void testLargeArrayNoMajority() {
        int[] arr = new int[10000];
        // Fill with 5000 of element 1 and 5000 of element 2
        for (int i = 0; i < 5000; i++) {
            arr[i] = 1;
        }
        for (int i = 5000; i < 10000; i++) {
            arr[i] = 2;
        }

        Integer result = algorithm.findMajority(arr);
        assertNull(result);
    }

    // ============ Position Tracking Tests ============

    @Test
    @DisplayName("Find majority with positions")
    void testFindMajorityWithPositions() {
        int[] arr = {3, 3, 4, 2, 4, 4, 2, 4, 4};
        BoyerMooreMajorityVote.MajorityResult result =
                algorithm.findMajorityWithPositions(arr);

        assertNotNull(result);
        assertEquals(4, result.getElement());
        assertEquals(5, result.getCount());

        int[] expectedPositions = {2, 4, 5, 7, 8};
        assertArrayEquals(expectedPositions, result.getPositions());
    }

    @Test
    @DisplayName("Find majority with positions - no majority")
    void testFindMajorityWithPositionsNoMajority() {
        int[] arr = {1, 2, 3, 4, 5};
        BoyerMooreMajorityVote.MajorityResult result =
                algorithm.findMajorityWithPositions(arr);

        assertNull(result);
    }

    // ============ Optimized Version Tests ============

    @Test
    @DisplayName("Optimized version - early termination")
    void testOptimizedEarlyTermination() {
        // Array where majority appears early
        int[] arr = new int[1000];
        for (int i = 0; i < 501; i++) {
            arr[i] = 99;
        }
        for (int i = 501; i < 1000; i++) {
            arr[i] = i;
        }

        Integer result = algorithm.findMajorityOptimized(arr);
        assertEquals(99, result);

        // Check that comparisons are less than 2*n (early termination worked)
        long comparisons = algorithm.getTracker().getComparisons();
        assertTrue(comparisons < 2000,
                "Expected early termination with fewer comparisons");
    }

    @Test
    @DisplayName("Optimized version - unanimous array")
    void testOptimizedUnanimous() {
        int[] arr = new int[1000];
        Arrays.fill(arr, 42);

        Integer result = algorithm.findMajorityOptimized(arr);
        assertEquals(42, result);

        // Should terminate very early
        long comparisons = algorithm.getTracker().getComparisons();
        assertTrue(comparisons < 1000,
                "Expected very early termination for unanimous array");
    }

    // ============ Parameterized Tests ============

    @ParameterizedTest
    @CsvSource({
            "3, '2,2,1,1,1,2,2'",
            "1, '1,1,1,2,3,4,5'",
            "5, '5,5,5,5,1,2,3'"
    })
    @DisplayName("Parameterized majority tests")
    void testParameterizedMajority(int expected, String arrayStr) {
        int[] arr = Arrays.stream(arrayStr.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        Integer result = algorithm.findMajority(arr);
        assertEquals(expected, result);
    }

    // ============ Property-Based Tests ============

    @Test
    @DisplayName("Property: If element appears > n/2 times, it must be found")
    void testPropertyMajorityMustBeFound() {
        Random random = new Random(42);

        for (int trial = 0; trial < 100; trial++) {
            int n = 10 + random.nextInt(90); // Array size 10-100
            int[] arr = new int[n];
            int majorityElement = random.nextInt(100);

            // Ensure majority: fill > n/2 positions with majorityElement
            int majorityCount = n / 2 + 1 + random.nextInt(n / 2);
            for (int i = 0; i < majorityCount; i++) {
                arr[i] = majorityElement;
            }

            // Fill rest with random elements
            for (int i = majorityCount; i < n; i++) {
                arr[i] = random.nextInt(100);
            }

            // Shuffle array
            shuffleArray(arr, random);

            Integer result = algorithm.findMajority(arr);
            assertEquals(majorityElement, result,
                    "Failed to find majority element in trial " + trial);
        }
    }

    @Test
    @DisplayName("Property: If no element appears > n/2 times, result is null")
    void testPropertyNoMajorityReturnsNull() {
        Random random = new Random(123);

        for (int trial = 0; trial < 100; trial++) {
            int n = 10 + random.nextInt(40);
            int[] arr = new int[n];

            // Create array where no element appears more than n/2 times
            int distinctElements = Math.max(3, n / 3);
            for (int i = 0; i < n; i++) {
                arr[i] = i % distinctElements;
            }

            shuffleArray(arr, random);

            Integer result = algorithm.findMajority(arr);

            // Verify no element actually appears > n/2 times
            Map<Integer, Integer> counts = new HashMap<>();
            for (int num : arr) {
                counts.put(num, counts.getOrDefault(num, 0) + 1);
            }

            boolean hasMajority = counts.values().stream()
                    .anyMatch(count -> count > n / 2);

            if (hasMajority) {
                assertNotNull(result, "Should find majority in trial " + trial);
            } else {
                assertNull(result, "Should not find majority in trial " + trial);
            }
        }
    }

    // ============ Performance Metrics Tests ============

    @Test
    @DisplayName("Verify O(n) time complexity")
    void testLinearTimeComplexity() {
        int[] arr = new int[10000];
        Arrays.fill(arr, 42);

        algorithm.findMajority(arr);

        long comparisons = algorithm.getTracker().getComparisons();
        long arrayAccesses = algorithm.getTracker().getArrayAccesses();

        // Should be approximately 2*n (two passes)
        assertTrue(arrayAccesses <= 20000 + 100,
                "Array accesses should be O(n)");
        assertTrue(comparisons <= 20000 + 100,
                "Comparisons should be O(n)");
    }

    @Test
    @DisplayName("Verify O(1) space complexity")
    void testConstantSpaceComplexity() {
        int[] sizes = {100, 1000, 10000};
        long[] memoryUsed = new long[sizes.length];

        for (int i = 0; i < sizes.length; i++) {
            int[] arr = new int[sizes[i]];
            Arrays.fill(arr, 42);
            algorithm.findMajority(arr);
            memoryUsed[i] = algorithm.getTracker().getMemoryUsed();
        }

        // Memory usage should not scale significantly with input size
        // (allowing for some JVM variance)
        assertTrue(Math.abs(memoryUsed[2] - memoryUsed[0]) < 100000,
                "Space complexity should be O(1)");
    }

    // ============ Helper Methods ============

    private void shuffleArray(int[] arr, Random random) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}