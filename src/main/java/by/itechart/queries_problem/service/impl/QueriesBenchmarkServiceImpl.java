package by.itechart.queries_problem.service.impl;

import by.itechart.queries_problem.db_queries_benchmark.BenchmarkResult;
import by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.BenchmarkExecutorProvider;
import by.itechart.queries_problem.service.QueriesBenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class QueriesBenchmarkServiceImpl implements QueriesBenchmarkService {

    private final BenchmarkExecutorProvider benchmarkExecutorProvider;

    private final int defaultNumberOfRepetitions = 10;

    @Override
    public Map<String, BenchmarkResult> executeBenchmarks(String query, Integer numberOfRepetitions) {
        return benchmarkExecutorProvider.getExecutorMap().entrySet()
                .parallelStream()
                .collect(Collectors.toConcurrentMap(
                        dbBenchmarkExecutorEntry -> dbBenchmarkExecutorEntry.getKey().getSimpleName(),
                        dbBenchmarkExecutorEntry ->
                                new BenchmarkResult(IntStream.range(0, numberOfRepetitions == null ? defaultNumberOfRepetitions : numberOfRepetitions)
                                        .mapToDouble(i -> dbBenchmarkExecutorEntry.getValue().executeBenchmark(query).getExecutionTimeInMillis())
                                        .average().orElse(0)
                                )
                ));
    }
}
