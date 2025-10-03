# Assignment 2: Linear Array Algorithms

**Course**: Algorithm Analysis and Design  
**Assignment**: Pair 3 - Linear Array Algorithms  
**Student A**: Boyer-Moore Majority Vote Algorithm  

## 📋 Project Overview

This project implements two fundamental linear-time array algorithms with comprehensive performance analysis, testing, and benchmarking capabilities.

### Algorithms Implemented

1. **Boyer-Moore Majority Vote** - Finds majority element (appearing > n/2 times) in O(n) time, O(1) space

## 🚀 Quick Start

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Build and Run

## 📁 Project Structure

```
assignment2-BoyerMooreMajorityVote/
├── src/
│   ├── main/java/org/example/
│   │   ├── algorithms/
│   │   │   └── BoyerMooreMajorityVote.java
│   │   ├── utils/
│   │   └── ├── PerformanceTracker.java
│   │       └── BenchmarkRunner.java
│   └── test/java/
│           └── BoyerMooreMajorityVoteTest.java
├── docs/
│   └── performance-plots/
├── pom.xml
└── README.md
```

## 🔬 Algorithm Details

### Boyer-Moore Majority Vote

**Problem**: Find element appearing more than n/2 times in array

**Algorithm**:
1. **Candidate Selection Phase**: Use voting mechanism to find potential candidate
2. **Verification Phase**: Verify candidate actually appears > n/2 times

**Complexity Analysis**:
- **Time Complexity**:
    - Best Case: Θ(n) - unanimous array, early termination possible
    - Average Case: Θ(n) - two complete passes
    - Worst Case: Θ(n) - two complete passes required
- **Space Complexity**: Θ(1) - only two variables needed

**Key Operations**:
- Comparisons: ~2n (two passes)
- Assignments: ~n
- Array Accesses: ~2n

**Optimizations**:
- Early termination when count > n/2 in first pass
- Single-pass verification when majority is obvious

## 🧪 Testing

### Test Coverage

Both algorithms include comprehensive test suites:

- ✅ **Basic Functionality**: Correct results on standard inputs
- ✅ **Edge Cases**: Empty, single element, boundary conditions
- ✅ **Large Arrays**: Scalability testing (10K+ elements)
- ✅ **Property-Based Tests**: Verification of algorithmic properties
- ✅ **Performance Tests**: Complexity verification
- ✅ **Correctness Validation**: Comparison with brute force

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BoyerMooreMajorityVoteTest

# Run with coverage
mvn test jacoco:report
```

### Test Statistics

**Boyer-Moore Tests**: 25+ test cases
- Edge cases: 8
- Functional tests: 10
- Property-based: 3
- Performance: 4

## 📊 Benchmarking

### Input Distributions

**Boyer-Moore**:
1. Clear Majority (60% of elements)
2. Slim Majority (51% of elements)
3. No Majority (uniform distribution)
4. Unanimous (100% same element)

### Default Test Sizes

- 100 elements
- 1,000 elements
- 10,000 elements
- 100,000 elements

### Performance Metrics Collected

- Execution time (milliseconds)
- Number of comparisons
- Number of assignments
- Array accesses
- Memory usage (bytes)
- Algorithm-specific results

## 📈 Expected Performance

### Boyer-Moore Majority Vote

| Input Size | Time (ms) | Comparisons | Growth Factor |
|------------|-----------|-------------|---------------|
| 100        | ~0.01     | ~200        | -             |
| 1,000      | ~0.1      | ~2,000      | ~1.0          |
| 10,000     | ~1.0      | ~20,000     | ~1.0          |
| 100,000    | ~10.0     | ~200,000    | ~1.0          |

**Growth Factor ≈ 1.0 confirms O(n) complexity**

## 🎯 Key Features

### Boyer-Moore Implementation
✅ Two-phase algorithm (candidate + verification)  
✅ Early termination optimization  
✅ Position tracking capability  
✅ Optimized version for unanimous arrays  
✅ Comprehensive metrics collection

## 🐛 Known Limitations

- JVM warmup can affect first measurements
- Memory measurements have ~10KB variance
- Very small arrays (n < 10) have measurement noise
- GC can interfere with timing measurements

## 📚 References

1. Boyer, R. S., & Moore, J. S. (1991). MJRTY - A Fast Majority Vote Algorithm
2. Kadane, J. (1984). Maximum Subarray Problem
3. Cormen, T. H., et al. (2009). Introduction to Algorithms (3rd ed.)

## 👥 Contributors

**Student A**: Syzdykov Muslim - Boyer-Moore Majority Vote