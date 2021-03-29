package by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.impl;

import by.itechart.queries_problem.db_queries_benchmark.ConnectionsHolder;
import by.itechart.queries_problem.db_queries_benchmark.DB;
import by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.BenchmarkExecutor;
import by.itechart.queries_problem.exception.BenchmarkResultParseException;
import by.itechart.queries_problem.exception.SQLExceptionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PostgreSQLBenchmarkExecutor implements BenchmarkExecutor {

    private final ConnectionsHolder connectionsHolder;
    private static final Pattern POSTGRES_EXECUTION_TIME_PATTERN =
            Pattern.compile("Execution Time: (?<executionTime>[\\d.]+) ms");

    @Override
    public double executeBenchmark(String query) {
        Connection connection = connectionsHolder.getConnection(DB.POSTGRESQL);

        double executionTime;

        try(PreparedStatement statement = connection.prepareStatement("explain analyze " + query)){
            ResultSet rs = statement.executeQuery();

            StringBuilder resultString = new StringBuilder();

            while(rs.next()) {
                resultString.append(rs.getString(1));
            }

            Matcher matcher = POSTGRES_EXECUTION_TIME_PATTERN.matcher(resultString.toString());

            if(matcher.find()) {
                executionTime = Double.parseDouble(matcher.group("executionTime"));
            } else {
                throw new BenchmarkResultParseException(query, DB.POSTGRESQL);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            throw new SQLExceptionWrapper(e, DB.POSTGRESQL);
        }

        return executionTime;
    }
}
