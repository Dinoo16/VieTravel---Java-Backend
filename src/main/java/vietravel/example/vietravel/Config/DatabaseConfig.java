package vietravel.example.vietravel.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        String jdbcUrl = "jdbc:mysql://{{RAILWAY_TCP_PROXY_DOMAIN}}:{{RAILWAY_TCP_PROXY_PORT}}/{{MYSQL_DATABASE}}" +
                "?useSSL=true&requireSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";

        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername("{{MYSQLUSER}}");
        dataSource.setPassword("{{MYSQL_ROOT_PASSWORD}}");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMaximumPoolSize(10);

        return dataSource;
    }
}
