package ru.netology.web.repository;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreditRequestEntityRepositoryPostgreSQL{
    QueryRunner runner = new QueryRunner();

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://185.119.57.47:5432/app", "app", "pass");
    }

    public int getStatusCount(String status) throws SQLException {
        var conn = getConnection();
        Long count = runner.query(
                conn,
                "SELECT COUNT(id) FROM credit_request_entity WHERE status = '" + status + "'",
                new ScalarHandler<Long>()
        );
        return count.intValue();
    }
}
