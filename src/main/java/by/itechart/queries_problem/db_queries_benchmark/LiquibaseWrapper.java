package by.itechart.queries_problem.db_queries_benchmark;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class LiquibaseWrapper {

    @Value("${spring.liquibase.change-log}")
    private String changeLogFile;

    public void rollupDBMigrations(Connection connection) {
        try {
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            throw new RuntimeException(e);
        }
    }
}
