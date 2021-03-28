package by.itechart.queries_problem.db_queries_benchmark;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

@Component
@Getter
public class ConnectionsHolder {

    private final String dbConnectionConfigPath = "/db_connection_properties";

    private final Map<DB, Connection> dbConnectionMap = new HashMap<>();

    @PostConstruct
    private void init() {
        DB[] dbs = DB.values();

        for(DB db: dbs) {
            Connection connection;

            try {
                String propertyFilePath = dbConnectionConfigPath + "/" + db.name().toLowerCase(Locale.ROOT) + ".properties";

                Properties properties = new Properties();
                properties.load(getClass().getResourceAsStream(propertyFilePath));

                connection = DriverManager.getConnection(
                        properties.getProperty("url"),
                        properties.getProperty("username"),
                        properties.getProperty("password")
                );
            } catch (SQLException | IOException throwables) {
                throw new RuntimeException(throwables);
            }

            dbConnectionMap.put(db, connection);
        }
    }

    public Connection getConnection(DB db) {
        return dbConnectionMap.get(db);
    }

}
