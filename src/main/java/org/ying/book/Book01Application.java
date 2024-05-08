package org.ying.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.ying.book.mapper")
public class Book01Application {

    public static void main(String[] args) {
        SpringApplication.run(Book01Application.class, args);
    }

}
