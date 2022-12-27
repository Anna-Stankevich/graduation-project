package ru.netology.web.repository;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PaymentEntityRepositoryMySQL {
    QueryRunner runner = new QueryRunner();

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://185.119.57.47:3306/app", "app", "pass");
    }

    public int getStatusCount(String status) throws SQLException {
        var conn = getConnection();
        Long count = runner.query(
                conn,
                "SELECT COUNT(id) FROM payment_entity WHERE status = '" + status + "'",
                new ScalarHandler<Long>()
        );
        return count.intValue();
    }
}
