package de.lightplugins.kimetsu.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

public class Connection {


    public HikariDataSource connectToDataBaseViaMariaDB(String databaseName) {

        Dotenv config = Dotenv.configure().load();

        System.out.println("[KIMETSU] Starting database connection ...");
        System.out.println("[KIMETSU] Reading database parameter from .env ...");

        String host = config.get("HOST");
        String port = config.get("PORT");
        String user = config.get("USER");
        String password = config.get("PASSWORD");
        Boolean ssl = Boolean.valueOf(config.get("SSL"));
        Boolean useServerPrepStmts = Boolean.valueOf(config.get("USE_SERVER_PREP_STMTS"));
        Boolean cachePrepStmts = Boolean.valueOf(config.get("CACHE_PREP_STMTS"));
        int prepStmtCacheSize = Integer.parseInt(config.get("PREP_STMT_CACHE_SIZE"));
        int prepStmtCacheSqlLimit = Integer.parseInt(config.get("PREP_STMT_CACHE_SQL_LIMIT"));
        int connectionPoolSize = Integer.parseInt(config.get("CONNECTION_POOL_SIZE"));

        System.out.println("[KIMETSU] Creating hikari config ...");
        HikariConfig hikariConfig = new HikariConfig();
        //hikariConfig.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
        hikariConfig.setJdbcUrl("jdbc:mariadb://" +host + ":" + port + "/" + databaseName);
        hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
        hikariConfig.addDataSourceProperty("serverName", host);
        hikariConfig.addDataSourceProperty("port", port);
        hikariConfig.addDataSourceProperty("databaseName", databaseName);
        hikariConfig.addDataSourceProperty("user", user);
        hikariConfig.addDataSourceProperty("password", password);
        hikariConfig.addDataSourceProperty("useSSL", ssl);
        hikariConfig.setMaximumPoolSize(connectionPoolSize);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", useServerPrepStmts);
        hikariConfig.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);

        return new HikariDataSource(hikariConfig);

    }
}
