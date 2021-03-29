package by.itechart.queries_problem.exception;

import by.itechart.queries_problem.db_queries_benchmark.DB;

public class BenchmarkResultParseException extends ApplicationRuntimeException {
    public BenchmarkResultParseException(String query, DB db) {
        super(String.format("unable to parse benchmark result of query \n %s \n from %s db response", query, db.name()));
    }
}
