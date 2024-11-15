package com.project.teamproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "Server.Entity")
@EnableJpaRepositories("Server.Repository")  // 리포지토리 스캔 범위 지정
@SpringBootApplication(scanBasePackages = {"com.project.teamproject", "Server"})
public class TeamProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamProjectApplication.class, args);
    }

}
