package by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.impl;

import by.itechart.queries_problem.db_queries_benchmark.BenchmarkResult;
import by.itechart.queries_problem.db_queries_benchmark.ConnectionsHolder;
import by.itechart.queries_problem.db_queries_benchmark.DB;
import by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.BenchmarkExecutor;
import by.itechart.queries_problem.exception.BenchmarkResultParseException;
import by.itechart.queries_problem.exception.SQLExceptionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
@RequiredArgsConstructor
public class H2DBBenchmarkExecutor implements BenchmarkExecutor {

    private final ConnectionsHolder connectionsHolder;
    private static final String GET_AVERAGE_EXECUTION_TIME_BY_SQL_STATEMENT =
            "select AVERAGE_EXECUTION_TIME from INFORMATION_SCHEMA.QUERY_STATISTICS where SQL_STATEMENT = ?";
    private static final String SET_STATISTICS = "set query_statistics true";

    @Override
    public BenchmarkResult executeBenchmark(String query) {
        Connection connection = connectionsHolder.getConnection(DB.H2DB);

        double executionTime;

        try (Statement statement = connection.createStatement();
             PreparedStatement queryStatement = connection.prepareStatement(query);
             PreparedStatement averageTimeStatement = connection.prepareStatement(GET_AVERAGE_EXECUTION_TIME_BY_SQL_STATEMENT)) {

            statement.executeUpdate(SET_STATISTICS);

            queryStatement.executeQuery();

            averageTimeStatement.setString(1, query);

            ResultSet rs = averageTimeStatement.executeQuery();

            if (rs.next()) {
                executionTime = Double.parseDouble(rs.getString("AVERAGE_EXECUTION_TIME"));
            } else {
                throw new BenchmarkResultParseException(query, DB.H2DB);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            throw new SQLExceptionWrapper(e, DB.H2DB);
        }

        return new BenchmarkResult(executionTime);
    }
}
