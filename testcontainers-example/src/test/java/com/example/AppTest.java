package com.example;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class AppTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>()
            .withDatabaseName("example");

    @Test
    void currentCatalog() throws Exception {
        try (Connection con = DriverManager.getConnection(postgres.getJdbcUrl(),
                postgres.getUsername(), postgres.getPassword());
                PreparedStatement pst = con.prepareStatement("SELECT current_catalog");
                ResultSet rs = pst.executeQuery()) {
            rs.next();
            final String currentCatalog = rs.getString(1);
            assertEquals("example", currentCatalog);
        }
    }

    @Test
    void isRunning() throws Exception {
        assertTrue(postgres.isRunning());
    }
}
