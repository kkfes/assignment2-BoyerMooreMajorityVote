package org.example.utils;

import org.example.algorithms.BoyerMooreMajorityVote;

import java.util.*;
import java.io.*;

/**
 * Command-line interface for benchmarking linear array algorithms
 * Supports both Boyer-Moore Majority Vote and Kadane's Algorithm
 */
public class BenchmarkRunner {

    private static final int[] DEFAULT_SIZES = {100, 1000, 10000, 100000};
    private static final int WARMUP_ITERATIONS = 5;
    private static final int MEASUREMENT_ITERATIONS = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Linear Array Algorithms Benchmark Tool ===\n");
        System.out.println("Select algorithm:");
        System.out.println("1. Boyer-Moore Majority Vote");

        int choice = scanner.nextInt();

        if (choice == 1) {
            runBoyerMooreBenchmark(scanner);
        }else {
            System.out.println("Invalid choice!");
            return;
        }

        scanner.close();
    }

    // ============ Boyer-Moore Benchmarks ============

    private static void runBoyerMooreBenchmark(Scanner scanner) {
        System.out.println("\n=== Boyer-Moore Majority Vote Benchmark ===\n");
        System.out.println("Select input type:");
        System.out.println("1. Clear Majority (60%)");
        System.out.println("2. Slim Majority (51%)");
        System.out.println("3. No Majority (uniform distribution)");
        System.out.println("4. Unanimous (100%)");
        System.out.println("5. Custom test");
        System.out.print("\nEnter choice: ");

        int inputType = scanner.nextInt();

        System.out.println("\nRunning benchmarks on sizes: " +
                Arrays.toString(DEFAULT_SIZES));
        System.out.println("Warmup iterations: " + WARMUP_ITERATIONS);
        System.out.println("Measurement iterations: " + MEASUREMENT_ITERATIONS);
        System.out.println("\nProcessing...\n");

        BoyerMooreMajorityVote algorithm = new BoyerMooreMajorityVote();

        try (PrintWriter csv = new PrintWriter(new FileWriter("boyer_moore_results.csv"))) {
            csv.println("InputSize,InputType,AvgTimeMs,StdDevMs,Comparisons," +
                    "Assignments,ArrayAccesses,MemoryBytes,Result");

            for (int size : DEFAULT_SIZES) {
                int[] testArray = generateBoyerMooreInput(size, inputType);
                BenchmarkResult result = benchmarkBoyerMoore(algorithm, testArray);

                String inputTypeName = getInputTypeName(inputType);
                csv.printf("%d,%s,%.4f,%.4f,%d,%d,%d,%d,%s%n",
                        size, inputTypeName,
                        result.avgTime, result.stdDev,
                        result.comparisons, result.assignments,
                        result.arrayAccesses, result.memoryUsed,
                        result.algorithmResult);

                System.out.printf("Size: %6d | Time: %8.4f ms | Comparisons: %10d | Result: %s%n",
                        size, result.avgTime, result.comparisons, result.algorithmResult);
            }

            System.out.println("\n✓ Results saved to boyer_moore_results.csv");

        } catch (IOException e) {
            System.err.println("Error writing results: " + e.getMessage());
        }
    }

    private static BenchmarkResult benchmarkBoyerMoore(
            BoyerMooreMajorityVote algorithm, int[] testArray) {

        // Warmup phase
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            int[] copy = Arrays.copyOf(testArray, testArray.length);
            algorithm.findMajority(copy);
        }

        // Measurement phase
        double[] times = new double[MEASUREMENT_ITERATIONS];
        long totalComparisons = 0;
        long totalAssignments = 0;
        long totalArrayAccesses = 0;
        long totalMemory = 0;
        Integer result = null;

        for (int i = 0; i < MEASUREMENT_ITERATIONS; i++) {
            int[] copy = Arrays.copyOf(testArray, testArray.length);
            result = algorithm.findMajority(copy);

            PerformanceTracker tracker = algorithm.getTracker();
            times[i] = tracker.getExecutionTimeMillis();
            totalComparisons += tracker.getComparisons();
            totalAssignments += tracker.getAssignments();
            totalArrayAccesses += tracker.getArrayAccesses();
            totalMemory += tracker.getMemoryUsed();
        }

        double avgTime = Arrays.stream(times).average().orElse(0);
        double stdDev = calculateStdDev(times, avgTime);

        return new BenchmarkResult(
                avgTime, stdDev,
                totalComparisons / MEASUREMENT_ITERATIONS,
                totalAssignments / MEASUREMENT_ITERATIONS,
                totalArrayAccesses / MEASUREMENT_ITERATIONS,
                totalMemory / MEASUREMENT_ITERATIONS,
                result != null ? result.toString() : "null"
        );
    }

    private static int[] generateBoyerMooreInput(int size, int inputType) {
        Random random = new Random(42);
        int[] arr = new int[size];

        switch (inputType) {
            case 1: // Clear Majority (60%)
                int majorityCount60 = (int) (size * 0.6);
                for (int i = 0; i < majorityCount60; i++) {
                    arr[i] = 1;
                }
                for (int i = majorityCount60; i < size; i++) {
                    arr[i] = random.nextInt(100) + 2;
                }
                break;

            case 2: // Slim Majority (51%)
                int majorityCount51 = size / 2 + 1;
                for (int i = 0; i < majorityCount51; i++) {
                    arr[i] = 1;
                }
                for (int i = majorityCount51; i < size; i++) {
                    arr[i] = random.nextInt(100) + 2;
                }
                break;

            case 3: // No Majority
                for (int i = 0; i < size; i++) {
                    arr[i] = i % (size / 3 + 1);
                }
                break;

            case 4: // Unanimous
                Arrays.fill(arr, 42);
                break;

            case 5: // Custom - user defined
                for (int i = 0; i < size; i++) {
                    arr[i] = random.nextInt(10);
                }
                break;
        }

        // Shuffle array
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        return arr;
    }
    private static List<PlotData> readCSV(String filename) throws IOException {
        List<PlotData> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int size = Integer.parseInt(parts[0]);
                double time = Double.parseDouble(parts[2]);
                data.add(new PlotData(size, time));
            }
        }

        return data;
    }

    // ============ Helper Methods ============

    private static double calculateStdDev(double[] values, double mean) {
        double sumSquaredDiff = 0;
        for (double value : values) {
            sumSquaredDiff += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sumSquaredDiff / values.length);
    }

    private static String getInputTypeName(int type) {
        switch (type) {
            case 1: return "ClearMajority60%";
            case 2: return "SlimMajority51%";
            case 3: return "NoMajority";
            case 4: return "Unanimous100%";
            case 5: return "Custom";
            default: return "Unknown";
        }
    }

    private static String getKadaneInputTypeName(int type) {
        switch (type) {
            case 1: return "AllPositive";
            case 2: return "AllNegative";
            case 3: return "MixedRandom";
            case 4: return "Alternating";
            case 5: return "SingleLargeElement";
            default: return "Unknown";
        }
    }

    // ============ Result Classes ============

    static class BenchmarkResult {
        double avgTime;
        double stdDev;
        long comparisons;
        long assignments;
        long arrayAccesses;
        long memoryUsed;
        String algorithmResult;

        BenchmarkResult(double avgTime, double stdDev, long comparisons,
                        long assignments, long arrayAccesses, long memoryUsed,
                        String algorithmResult) {
            this.avgTime = avgTime;
            this.stdDev = stdDev;
            this.comparisons = comparisons;
            this.assignments = assignments;
            this.arrayAccesses = arrayAccesses;
            this.memoryUsed = memoryUsed;
            this.algorithmResult = algorithmResult;
        }
    }

    static class PlotData {
        int size;
        double time;

        PlotData(int size, double time) {
            this.size = size;
            this.time = time;
        }
    }
}