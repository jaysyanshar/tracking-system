package com.teleport.tracking.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.teleport.tracking.system.*"})
@EntityScan(basePackages = "com.teleport.tracking.system.entity.dao")
public class Main {
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}