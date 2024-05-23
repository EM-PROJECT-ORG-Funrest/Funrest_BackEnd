package com.example.app.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataSourceConfigTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void dataSourceConfigTest() throws SQLException {
        System.out.println(dataSource);
        System.out.println(dataSource.getConnection());
    }

}