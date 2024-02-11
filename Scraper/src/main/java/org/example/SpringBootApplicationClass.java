package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.ServiceImpl.ProxySetup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = "org.example")
@Slf4j
public class SpringBootApplicationClass {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationClass.class,args);
    }
}
