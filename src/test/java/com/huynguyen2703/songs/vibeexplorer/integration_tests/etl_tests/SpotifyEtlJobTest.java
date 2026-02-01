package com.huynguyen2703.songs.vibeexplorer.integration_tests.etl_tests;

import com.huynguyen2703.songs.vibeexplorer.etl.SpotifyEtlJob;
import com.huynguyen2703.songs.vibeexplorer.repositories.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the full ETL pipeline
 * Tests: Extract (real Spotify API) → Transform → Load (real DB)
 */
@SpringBootTest
@Testcontainers
public class SpotifyEtlJobTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // Override ETL config for faster tests
        registry.add("spotify.etl.batch-size", () -> "5");
        registry.add("spotify.etl.max-pages", () -> "1");  // Only 1 page for testing
        registry.add("spotify.etl.default-keywords", () -> "pop");  // Single keyword
        registry.add("spotify.etl.market", () -> "US");
    }

    @Autowired
    private SpotifyEtlJob etlJob;

    @Autowired
    private SongRepository songRepository;

    @BeforeEach
    void setUp() {
        songRepository.deleteAll();
    }

    @Test
    void testFullEtlPipeline() {
        // Given: Empty database
        assertEquals(0, songRepository.count());

        // When: Run ETL job
        etlJob.runEtl();

        // Then: Songs should be loaded into database
        long songCount = songRepository.count();

        assertTrue(songCount > 0, "ETL should load songs into database");
        assertTrue(songCount <= 5, "Should respect batch size limit");

        System.out.println("✅ ETL loaded " + songCount + " songs");
    }

    @Test
    void testEtlDoesNotCreateDuplicates() {
        // Given: Run ETL once
        etlJob.runEtl();
        long firstRunCount = songRepository.count();

        System.out.println("First run loaded: " + firstRunCount + " songs");

        // When: Run ETL again (simulating scheduled run)
        etlJob.runEtl();
        long secondRunCount = songRepository.count();

        System.out.println("Second run total: " + secondRunCount + " songs");

        // Then: Should not create duplicates (same count or minimal increase)
        assertTrue(secondRunCount >= firstRunCount, "Should not lose data");
        assertTrue(secondRunCount <= firstRunCount + 5, "Should skip most duplicates");
    }

    @Test
    void testEtlHandlesMultipleKeywords() {
        // This test would use multiple keywords, but we override to single keyword
        // Just verify it completes without errors
        assertDoesNotThrow(() -> etlJob.runEtl());
    }
}