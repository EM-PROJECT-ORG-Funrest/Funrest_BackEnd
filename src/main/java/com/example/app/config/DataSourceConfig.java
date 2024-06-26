package com.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

// application.properties 에 직접 데이터베이스 연결 정보를 포함시키는 대신
// Java Config 파일을 사용하여 데이터베이스 연결 정보를 설정
// 이렇게하면 데이터베이스 연결 정보를 코드로 관리하고
// 필요에 따라 데이터베이스 설정을 변경하기가 더 쉬워짐
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.3.40:3306/funrestdb");
        dataSource.setUsername("user1");
        dataSource.setPassword("ghQkdaos12!");

        return dataSource;
    }
}
