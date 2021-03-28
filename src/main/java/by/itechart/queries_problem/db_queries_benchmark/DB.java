package by.itechart.queries_problem.db_queries_benchmark;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DB {
    POSTGRESQL("PostgreSQL"),
    MYSQL("MySQL");
    //ORACLEDB("OracleDB");

    private final String simpleName;
}
