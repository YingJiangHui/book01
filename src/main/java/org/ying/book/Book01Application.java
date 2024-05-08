package org.ying.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ying.book.configuration.SecurityConfig;

@SpringBootApplication
@MapperScan("org.ying.book.mapper")
@ImportAutoConfiguration(SecurityConfig.class)
public class Book01Application {

    public static void main(String[] args) {
        SpringApplication.run(Book01Application.class, args);
    }

}
