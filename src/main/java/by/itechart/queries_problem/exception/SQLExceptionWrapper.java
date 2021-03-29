package by.itechart.queries_problem.exception;

import by.itechart.queries_problem.db_queries_benchmark.DB;

import java.sql.SQLException;

public class SQLExceptionWrapper extends ApplicationRuntimeException {
    public SQLExceptionWrapper(SQLException cause, DB db) {
        super(String.format("Following exception occurred during %s db interaction:\n %s", db.name(), cause.getMessage()), cause);
    }
}
