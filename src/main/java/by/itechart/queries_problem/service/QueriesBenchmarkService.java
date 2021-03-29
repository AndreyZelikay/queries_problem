package by.itechart.queries_problem.service;

import by.itechart.queries_problem.db_queries_benchmark.BenchmarkResult;

import java.util.Map;

public interface QueriesBenchmarkService {

    Map<String, BenchmarkResult> executeBenchmarks(String query, Integer numberOfRepetitions);

}
