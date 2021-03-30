package by.itechart.queries_problem.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BenchmarkResult {
    private double executionTimeInMillis;
    private Integer returnedRowsCount;
    private Integer updatedRowsCount;
    private int numberOfRepetitions;
}
