package by.itechart.queries_problem.db_queries_benchmark.benchmark_executor;

import by.itechart.queries_problem.db_queries_benchmark.DB;
import by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.impl.MySQLBenchmarkExecutor;
import by.itechart.queries_problem.db_queries_benchmark.benchmark_executor.impl.PostgreSQLBenchmarkExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BenchmarkExecutorProvider {

    private final Map<DB, BenchmarkExecutor> executorMap = new HashMap<>();
    private final ApplicationContext applicationContext;

    @PostConstruct
    private void init(){
        executorMap.put(DB.POSTGRESQL, applicationContext.getBean(PostgreSQLBenchmarkExecutor.class));
        executorMap.put(DB.MYSQL, applicationContext.getBean(MySQLBenchmarkExecutor.class));
    }

    public Map<DB, BenchmarkExecutor> getExecutorMap() {
        return executorMap;
    }
}
