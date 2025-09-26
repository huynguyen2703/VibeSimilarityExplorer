package com.huynguyen2703.songs.vibeexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})

public class VibeSimilarityExplorerApplication {
    public static void main(String[] args) {
        SpringApplication.run(VibeSimilarityExplorerApplication.class, args);
    }
}
