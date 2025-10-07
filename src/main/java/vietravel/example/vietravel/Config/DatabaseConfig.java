package vietravel.example.vietravel.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        // Sử dụng các biến tiêu chuẩn của Railway
        String host = System.getenv("MYSQLHOST");
        String port = System.getenv("MYSQLPORT");
        String database = System.getenv("MYSQLDATABASE");
        String username = System.getenv("MYSQLUSER");
        String password = System.getenv("MYSQLPASSWORD");

        // Fallback: sử dụng các biến alternative
        if (host == null) {
            host = System.getenv("MYSQL_HOST");
            port = System.getenv("MYSQL_PORT");
            database = System.getenv("MYSQL_DATABASE");
            username = System.getenv("MYSQL_USER");
            password = System.getenv("MYSQL_PASSWORD");
        }

        // Fallback cuối cùng: development
        if (host == null) {
            log.warn("Using development database configuration");
            return createDevelopmentDataSource();
        }

        String jdbcUrl = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=true&requireSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                host, port, database
        );

        log.info("Database configured - Host: {}, DB: {}", host, database);

        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMaximumPoolSize(10);

        return dataSource;
    }

    private DataSource createDevelopmentDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/vie_travel?createDatabaseIfNotExist=true");
        dataSource.setUsername("root");
        dataSource.setPassword("trong2003");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }
}