package com.team10;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.team10.mapper"})
public class BankSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankSysApplication.class, args);
    }
}
