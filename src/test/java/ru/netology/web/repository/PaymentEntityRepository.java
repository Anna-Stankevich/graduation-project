package ru.netology.web.repository;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PaymentEntityRepository {
    QueryRunner runner = new QueryRunner();
    @SneakyThrows
    Connection getConnection() {
        return DriverManager.getConnection(System.getProperty("datasource.url"), System.getProperty("datasource.username"), System.getProperty("datasource.password"));
    }
    @SneakyThrows
    public int getStatusCount(String status) {
        var conn = getConnection();
        Long count = runner.query(
                conn,
                "SELECT COUNT(id) FROM payment_entity WHERE status = '" + status + "'",
                new ScalarHandler<Long>()
        );
        return count.intValue();
    }
}
