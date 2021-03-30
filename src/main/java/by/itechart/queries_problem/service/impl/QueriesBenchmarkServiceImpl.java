package by.itechart.queries_problem.service.impl;

import by.itechart.queries_problem.db_queries_benchmark.ConnectionsHolder;
import by.itechart.queries_problem.exception.GlobalExceptionHandler;
import by.itechart.queries_problem.exception.SQLExceptionWrapper;
import by.itechart.queries_problem.service.QueriesBenchmarkService;
import by.itechart.queries_problem.service.model.BenchmarkResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static int defaultNumberOfRepetitions = 10;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Map<String, BenchmarkResult> executeBenchmarks(String query, Integer numberOfRepetitions) {
        return connectionsHolder.getDbConnectionMap().entrySet()
                .parallelStream()
                .collect(Collectors.toConcurrentMap(
                        dbConnectionEntry -> dbConnectionEntry.getKey().getSimpleName(),
                        dbConnectionEntry -> {
                            try {
                                return new BenchmarkResult(
                                        measureAverageExecutionTime(query, dbConnectionEntry.getValue(), numberOfRepetitions)
                                );
                            } catch (SQLException e) {
                                throw new SQLExceptionWrapper(e, dbConnectionEntry.getKey());
                            }
                        })
                );
    }

    private double measureAverageExecutionTime(String query, Connection connection, Integer numberOfRepetitions) throws SQLException {
        numberOfRepetitions = numberOfRepetitions == null ? defaultNumberOfRepetitions : numberOfRepetitions;

        double totalExecutionTime = 0;
        int rowCountTotal = 0;

        for (int i = 0; i <= numberOfRepetitions; i++) {
            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                long startTime = System.nanoTime();

                ResultSet rs = preparedStatement.executeQuery();

                double executionTime = (System.nanoTime() - startTime) / 1_000_000.;

                if(executionTime == 0) {
                    numberOfRepetitions++;
                }

                totalExecutionTime += executionTime;

                if(rs.last()) {
                    rowCountTotal += rs.getRow();
                }
            }
        }

        logger.info("row count {}", rowCountTotal / numberOfRepetitions);

        return totalExecutionTime / numberOfRepetitions;
    }
}
