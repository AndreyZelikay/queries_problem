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
public class MySQLBenchmarkExecutor implements BenchmarkExecutor {


    private final ConnectionsHolder connectionsHolder;
    private static final Pattern MYSQL_EXECUTION_TIME_PATTERN =
            Pattern.compile("actual time=[\\d.]+?\\.\\.(?<executionTime>[\\d.]+) rows");

    @Override
    public double executeBenchmark(String query) {
        Connection connection = connectionsHolder.getConnection(DB.MYSQL);

        double executionTime;

        try(PreparedStatement statement = connection.prepareStatement("explain analyze " + query)){
            ResultSet rs = statement.executeQuery();
            rs.next();

            Matcher matcher = MYSQL_EXECUTION_TIME_PATTERN.matcher(rs.getString(1));

            if(matcher.find()) {
                executionTime = Double.parseDouble(matcher.group("executionTime"));
            } else {
                throw new BenchmarkResultParseException(query, DB.MYSQL);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            throw new SQLExceptionWrapper(e, DB.MYSQL);
        }

        return executionTime;
    }
}
