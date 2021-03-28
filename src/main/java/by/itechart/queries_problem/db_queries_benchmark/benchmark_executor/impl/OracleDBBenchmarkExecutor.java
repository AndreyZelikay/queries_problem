package by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.impl;

import by.itechart.queries_problem.db_queries_benchmark.BenchmarkResult;
import by.itechart.queries_problem.db_queries_benchmark.ConnectionsHolder;
import by.itechart.queries_problem.db_queries_benchmark.DB;
import by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.BenchmarkExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
@RequiredArgsConstructor
public class OracleDBBenchmarkExecutor implements BenchmarkExecutor {

    private final ConnectionsHolder connectionsHolder;
    private static final String GET_ELAPSED_TIME_BY_QUERY = "select ELAPSED_TIME from \"PUBLIC\".V$SQL where dbms_lob.compare(SQL_FULLTEXT, ?) = 0";

    @Override
    public BenchmarkResult executeBenchmark(String query) {
        Connection connection = connectionsHolder.getConnection(DB.POSTGRESQL);

        double executionTime;

        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ELAPSED_TIME_BY_QUERY)) {
            statement.executeQuery(query);

            preparedStatement.setString(1, query);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                executionTime = Long.parseLong(rs.getString("ELAPSED_TIME")) / 1000.;
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new BenchmarkResult(executionTime);
    }
}
