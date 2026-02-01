package com.huynguyen2703.songs.vibeexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class VibeSimilarityExplorerApplication {
    public static void main(String[] args) {
        SpringApplication.run(VibeSimilarityExplorerApplication.class, args);
    }
}
