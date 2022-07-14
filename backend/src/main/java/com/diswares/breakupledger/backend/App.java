package com.diswares.breakupledger.backend;

import com.group.cw.absol.annotions.EnableAuthProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 4everlynn
 */
@SpringBootApplication
@MapperScan("com.diswares.breakupledger.backend.mapper")
@EnableAuthProxy
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
