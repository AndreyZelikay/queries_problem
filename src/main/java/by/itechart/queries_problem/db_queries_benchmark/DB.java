package by.itechart.queries_problem.db_queries_benchmark;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DB {
    POSTGRESQL("PostgreSQL"),
    MYSQL("MySQL"),
    H2DB("H2DB");

    private final String simpleName;
}
