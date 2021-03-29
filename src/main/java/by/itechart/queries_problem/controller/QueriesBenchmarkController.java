package by.itechart.queries_problem.controller;

import by.itechart.queries_problem.db_queries_benchmark.BenchmarkResult;
import by.itechart.queries_problem.service.QueriesBenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QueriesBenchmarkController {

    private final QueriesBenchmarkService queriesBenchmarkService;

    @GetMapping
    public Map<String, BenchmarkResult> getResult(@RequestParam("query") String query,
                                                  @RequestParam(value = "numberOfRepetitions", required = false) Integer numberOfRepetitions) {
        return queriesBenchmarkService.executeBenchmarks(query, numberOfRepetitions);
    }
}

