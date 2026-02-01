package com.huynguyen2703.songs.vibeexplorer.functional_tests.unit_tests.etl_tests;

import com.huynguyen2703.songs.vibeexplorer.etl.SpotifyEtlConfig;
import com.huynguyen2703.songs.vibeexplorer.etl.SpotifyEtlJob;
import com.huynguyen2703.songs.vibeexplorer.etl.SpotifyEtlScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * Unit test for SpotifyEtlScheduler
 * Tests scheduling logic without waiting for cron
 */
public class SpotifyEtlSchedulerTest {

    @Mock
    private SpotifyEtlJob mockEtlJob;

    @Mock
    private SpotifyEtlConfig mockConfig;

    private SpotifyEtlScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scheduler = new SpotifyEtlScheduler(mockEtlJob, mockConfig);
    }

    @Test
    void testScheduledRunExecutesWhenEnabled() {
        // Given: Scheduling is enabled
        when(mockConfig.getSchedulingEnabled()).thenReturn(true);

        // When: Scheduled method is triggered
        scheduler.runScheduleEtl();

        // Then: An ETL job should run
        verify(mockEtlJob, times(1)).runEtl();
    }

    @Test
    void testScheduledRunSkipsWhenDisabled() {
        // Given: Scheduling is disabled
        when(mockConfig.getSchedulingEnabled()).thenReturn(false);

        // When: Scheduled method is triggered
        scheduler.runScheduleEtl();

        // Then: ETL job should NOT run
        verify(mockEtlJob, never()).runEtl();
    }

    @Test
    void testOverlapProtection() {
        // Given: Scheduling is enabled
        when(mockConfig.getSchedulingEnabled()).thenReturn(true);

        // Simulate a long-running ETL job
        doAnswer(invocation -> {
            Thread.sleep(100); // Simulate work
            return null;
        }).when(mockEtlJob).runEtl();

        // When: Two runs are triggered simultaneously
        Thread thread1 = new Thread(() -> scheduler.runScheduleEtl());
        Thread thread2 = new Thread(() -> scheduler.runScheduleEtl());

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Then: ETL should only run once (overlap prevented)
        verify(mockEtlJob, times(1)).runEtl();
    }

    @Test
    void testScheduledRunHandlesErrors() {
        // Given: Scheduling is enabled but ETL throws error
        when(mockConfig.getSchedulingEnabled()).thenReturn(true);
        doThrow(new RuntimeException("Simulated error")).when(mockEtlJob).runEtl();

        // When: Scheduled method is triggered
        // Then: Should not crash, just log error
        scheduler.runScheduleEtl();

        // Verify it tried to run
        verify(mockEtlJob, times(1)).runEtl();
    }
}