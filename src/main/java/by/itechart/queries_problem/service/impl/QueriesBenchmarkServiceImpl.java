package by.itechart.queries_problem.service.impl;

import by.itechart.queries_problem.db_queries_benchmark.ConnectionsHolder;
import by.itechart.queries_problem.db_queries_benchmark.DB;
import by.itechart.queries_problem.exception.SQLExceptionWrapper;
import by.itechart.queries_problem.service.QueriesBenchmarkService;
import by.itechart.queries_problem.service.model.BenchmarkResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QueriesBenchmarkServiceImpl implements QueriesBenchmarkService {

    private final ConnectionsHolder connectionsHolder;

    private static final int defaultNumberOfRepetitions = 10;
    private static final String DMLQueryRegex = "(insert|update|delete).+";

    @Override
    public Map<String, BenchmarkResult> executeBenchmarks(String query, Integer numberOfRepetitions) {
        return connectionsHolder.getDbConnectionMap().entrySet()
                .parallelStream()
                .collect(Collectors.toConcurrentMap(
                        dbConnectionEntry -> dbConnectionEntry.getKey().getSimpleName(),
                        dbConnectionEntry -> executeBenchmark(query,
                                dbConnectionEntry.getKey(),
                                dbConnectionEntry.getValue(),
                                numberOfRepetitions
                        ))
                );
    }

    private BenchmarkResult executeBenchmark(String query, DB db, Connection connection, Integer numberOfRepetitions) {
        numberOfRepetitions = numberOfRepetitions == null ? defaultNumberOfRepetitions : numberOfRepetitions;

        try {
            if (query.matches(DMLQueryRegex)) {
                return executeBenchmarkOnDMLQuery(query, connection, numberOfRepetitions);
            } else {
                return executeBenchmarkOnSelectQuery(query, connection, numberOfRepetitions);
            }
        } catch (SQLException e) {
            throw new SQLExceptionWrapper(e, db);
        }
    }

    private BenchmarkResult executeBenchmarkOnSelectQuery(String query, Connection connection, int numberOfRepetitions) throws SQLException {
        double totalExecutionTime = 0;
        int rowCountTotal = 0;

        int representativeMeasurementsNumber = 0;

        while (representativeMeasurementsNumber != numberOfRepetitions) {
            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                long startTime = System.nanoTime();

                ResultSet rs = preparedStatement.executeQuery();

                long executionTimeInNanos = System.nanoTime() - startTime;

                if (executionTimeInNanos != 0) {
                    totalExecutionTime += executionTimeInNanos / 1_000_000.;

                    if (rs.last()) {
                        rowCountTotal += rs.getRow();
                    }

                    representativeMeasurementsNumber++;
                }
            }
        }

        return new BenchmarkResult(totalExecutionTime / numberOfRepetitions,
                rowCountTotal / numberOfRepetitions,
                null,
                numberOfRepetitions
        );
    }

    private BenchmarkResult executeBenchmarkOnDMLQuery(String query, Connection connection, int numberOfRepetitions) throws SQLException {
        double totalExecutionTime = 0;
        int rowCountTotal = 0;

        int representativeMeasurementsNumber = 0;

        while (representativeMeasurementsNumber != numberOfRepetitions) {
            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
                long startTime = System.nanoTime();

                int rowCount = preparedStatement.executeUpdate();

                long executionTimeInNanos = System.nanoTime() - startTime;

                if (executionTimeInNanos != 0) {
                    totalExecutionTime += executionTimeInNanos / 1_000_000.;
                    rowCountTotal += rowCount;

                    representativeMeasurementsNumber++;
                }
            }
        }

        return new BenchmarkResult(totalExecutionTime / numberOfRepetitions,
                null,
                rowCountTotal / numberOfRepetitions,
                numberOfRepetitions
        );
    }
}
