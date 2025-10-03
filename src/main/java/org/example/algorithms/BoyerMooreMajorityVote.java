package org.example.algorithms;

import org.example.utils.PerformanceTracker;

/**
 * Boyer-Moore Majority Vote Algorithm
 *
 * Finds the majority element in an array (element appearing > n/2 times).
 * Uses a single-pass voting mechanism with O(n) time and O(1) space.
 *
 * Algorithm:
 * 1. Candidate Selection Phase: Find potential majority candidate
 * 2. Verification Phase: Verify the candidate is actually majority
 *
 * Complexity:
 * - Time: O(n) - two linear passes
 * - Space: O(1) - constant extra space
 */
public class BoyerMooreMajorityVote {

    private PerformanceTracker tracker;

    public BoyerMooreMajorityVote() {
        this.tracker = new PerformanceTracker("Boyer-Moore Majority Vote");
    }

    /**
     * Find majority element in array (element appearing > n/2 times)
     *
     * @param arr input array
     * @return majority element, or null if no majority exists
     * @throws IllegalArgumentException if array is null or empty
     */
    public Integer findMajority(int[] arr) {
        validateInput(arr);

        tracker.reset();
        tracker.startMeasurement(arr.length);

        // Phase 1: Find candidate
        Integer candidate = findCandidate(arr);

        // Phase 2: Verify candidate
        boolean isMajority = verifyCandidate(arr, candidate);

        tracker.stopMeasurement();

        return isMajority ? candidate : null;
    }

    /**
     * Phase 1: Find potential majority candidate using voting
     *
     * @param arr input array
     * @return potential majority candidate
     */
    private Integer findCandidate(int[] arr) {
        Integer candidate = null;
        int count = 0;

        for (int i = 0; i < arr.length; i++) {
            tracker.incrementArrayAccesses(); // Access arr[i]
            int current = arr[i];

            if (count == 0) {
                candidate = current;
                tracker.incrementAssignments(); // Assign candidate
                count = 1;
            } else {
                tracker.incrementComparisons(); // Compare with candidate
                if (current == candidate) {
                    count++;
                } else {
                    count--;
                }
            }
        }

        return candidate;
    }

    /**
     * Phase 2: Verify if candidate is actual majority (appears > n/2 times)
     *
     * @param arr input array
     * @param candidate potential majority element
     * @return true if candidate is majority, false otherwise
     */
    private boolean verifyCandidate(int[] arr, Integer candidate) {
        if (candidate == null) {
            return false;
        }

        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            tracker.incrementArrayAccesses(); // Access arr[i]
            tracker.incrementComparisons(); // Compare with candidate

            if (arr[i] == candidate) {
                count++;
            }
        }

        tracker.incrementComparisons(); // Final comparison with n/2
        return count > arr.length / 2;
    }

    /**
     * Find majority element with position tracking
     * Returns the majority element and all its positions
     *
     * @param arr input array
     * @return MajorityResult containing element and positions, or null
     */
    public MajorityResult findMajorityWithPositions(int[] arr) {
        validateInput(arr);

        Integer majority = findMajority(arr);
        if (majority == null) {
            return null;
        }

        // Find all positions of majority element
        int[] positions = new int[arr.length];
        int posCount = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == majority) {
                positions[posCount++] = i;
            }
        }

        // Trim positions array
        int[] trimmedPositions = new int[posCount];
        System.arraycopy(positions, 0, trimmedPositions, 0, posCount);

        return new MajorityResult(majority, trimmedPositions, posCount);
    }

    /**
     * Optimized version for nearly-unanimous arrays
     * Early termination when majority is obvious
     *
     * @param arr input array
     * @return majority element or null
     */
    public Integer findMajorityOptimized(int[] arr) {
        validateInput(arr);

        tracker.reset();
        tracker.startMeasurement(arr.length);

        Integer candidate = null;
        int count = 0;
        int threshold = arr.length / 2;

        // Phase 1 with early termination
        for (int i = 0; i < arr.length; i++) {
            tracker.incrementArrayAccesses();
            int current = arr[i];

            if (count == 0) {
                candidate = current;
                tracker.incrementAssignments();
                count = 1;
            } else {
                tracker.incrementComparisons();
                if (current == candidate) {
                    count++;
                    // Early termination: if count > n/2, we found majority
                    if (count > threshold) {
                        tracker.stopMeasurement();
                        return candidate;
                    }
                } else {
                    count--;
                }
            }
        }

        // Phase 2: Verify (only if early termination didn't occur)
        boolean isMajority = verifyCandidate(arr, candidate);
        tracker.stopMeasurement();

        return isMajority ? candidate : null;
    }

    /**
     * Validate input array
     */
    private void validateInput(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }
    }

    public PerformanceTracker getTracker() {
        return tracker;
    }

    /**
     * Result class containing majority element and its positions
     */
    public static class MajorityResult {
        private final int element;
        private final int[] positions;
        private final int count;

        public MajorityResult(int element, int[] positions, int count) {
            this.element = element;
            this.positions = positions;
            this.count = count;
        }

        public int getElement() { return element; }
        public int[] getPositions() { return positions; }
        public int getCount() { return count; }

        @Override
        public String toString() {
            return String.format("Majority Element: %d (appears %d times)",
                    element, count);
        }
    }
}