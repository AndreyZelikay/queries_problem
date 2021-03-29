package by.itechart.queries_problem.service;

import by.itechart.queries_problem.service.model.BenchmarkResult;

import java.util.Map;

public interface QueriesBenchmarkService {

    Map<String, BenchmarkResult> executeBenchmarks(String query, Integer numberOfRepetitions);

}
