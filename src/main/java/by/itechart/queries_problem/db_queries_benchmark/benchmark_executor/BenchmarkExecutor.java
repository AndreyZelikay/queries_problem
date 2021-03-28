package by.itechart.queries_problem.db_queries_benchmark.benchmark_executor;

import by.itechart.queries_problem.db_queries_benchmark.BenchmarkResult;

public interface BenchmarkExecutor {
    BenchmarkResult executeBenchmark(String query);
}
