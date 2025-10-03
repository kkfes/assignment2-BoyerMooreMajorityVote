package org.example.utils;

import java.io.*;
import org.apache.commons.csv.*;

/**
 * Tracks and collects performance metrics for algorithm analysis.
 * Includes counters for comparisons, assignments, array accesses, and timing.
 */
public class PerformanceTracker {
    private long comparisons;
    private long assignments;
    private long arrayAccesses;
    private long startTime;
    private long endTime;
    private long memoryBefore;
    private long memoryAfter;
    private String algorithmName;
    private int inputSize;

    public PerformanceTracker(String algorithmName) {
        this.algorithmName = algorithmName;
        reset();
    }

    /**
     * Reset all counters to zero
     */
    public void reset() {
        comparisons = 0;
        assignments = 0;
        arrayAccesses = 0;
        startTime = 0;
        endTime = 0;
        memoryBefore = 0;
        memoryAfter = 0;
    }

    /**
     * Start timing and memory measurement
     */
    public void startMeasurement(int inputSize) {
        this.inputSize = inputSize;
        System.gc(); // Suggest garbage collection
        try {
            Thread.sleep(100); // Give GC time to run
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Runtime runtime = Runtime.getRuntime();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
    }

    /**
     * Stop timing and memory measurement
     */
    public void stopMeasurement() {
        endTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
    }

    // Increment methods
    public void incrementComparisons() { comparisons++; }
    public void incrementComparisons(int count) { comparisons += count; }
    public void incrementAssignments() { assignments++; }
    public void incrementAssignments(int count) { assignments += count; }
    public void incrementArrayAccesses() { arrayAccesses++; }
    public void incrementArrayAccesses(int count) { arrayAccesses += count; }

    // Getters
    public long getComparisons() { return comparisons; }
    public long getAssignments() { return assignments; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getExecutionTimeNanos() { return endTime - startTime; }
    public double getExecutionTimeMillis() { return (endTime - startTime) / 1_000_000.0; }
    public long getMemoryUsed() { return memoryAfter - memoryBefore; }

    /**
     * Print summary of collected metrics
     */
    public void printSummary() {
        System.out.println("\n=== Performance Metrics for " + algorithmName + " ===");
        System.out.println("Input Size: " + inputSize);
        System.out.println("Execution Time: " + getExecutionTimeMillis() + " ms");
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Assignments: " + assignments);
        System.out.println("Array Accesses: " + arrayAccesses);
        System.out.println("Memory Used: " + getMemoryUsed() + " bytes");
        System.out.println("==========================================\n");
    }

    /**
     * Export metrics to CSV file
     */
    public void exportToCSV(String filename) throws IOException {
        File file = new File(filename);
        boolean fileExists = file.exists();

        try (FileWriter fw = new FileWriter(file, true);
             CSVPrinter printer = new CSVPrinter(fw, CSVFormat.DEFAULT)) {

            // Write header if file is new
            if (!fileExists) {
                printer.printRecord("Algorithm", "InputSize", "TimeMs",
                        "Comparisons", "Assignments", "ArrayAccesses", "MemoryBytes");
            }

            // Write data
            printer.printRecord(
                    algorithmName,
                    inputSize,
                    getExecutionTimeMillis(),
                    comparisons,
                    assignments,
                    arrayAccesses,
                    getMemoryUsed()
            );
        }
    }

    /**
     * Get metrics as a formatted string
     */
    public String getMetricsString() {
        return String.format(
                "n=%d, time=%.3fms, cmp=%d, assign=%d, access=%d, mem=%dB",
                inputSize, getExecutionTimeMillis(), comparisons,
                assignments, arrayAccesses, getMemoryUsed()
        );
    }
}