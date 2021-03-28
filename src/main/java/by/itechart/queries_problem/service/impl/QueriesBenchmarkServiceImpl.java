package by.itechart.queries_problem.service.impl;

import by.itechart.queries_problem.db_queries_benchmark.BenchmarkResult;
import by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.BenchmarkExecutorProvider;
import by.itechart.queries_problem.service.QueriesBenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QueriesBenchmarkServiceImpl implements QueriesBenchmarkService {

    private final BenchmarkExecutorProvider benchmarkExecutorProvider;

    @Override
    public Map<String, BenchmarkResult> executeBenchmarks(String query) {
        return benchmarkExecutorProvider.getExecutorMap().entrySet()
                .parallelStream()
                .collect(Collectors.toMap(
                        dbBenchmarkExecutorEntry -> dbBenchmarkExecutorEntry.getKey().getSimpleName(),
                        dbBenchmarkExecutorEntry -> dbBenchmarkExecutorEntry.getValue().executeBenchmark(query)
                ));
    }
}
