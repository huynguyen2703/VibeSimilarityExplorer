package com.huynguyen2703.songs.vibeexplorer.etl;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.concurrent.atomic.AtomicBoolean;


/*
    Responsibility:

    Trigger an ETL job on a schedule

    TODOs:

    Use @Scheduled

    Decide a cron interval (e.g., nightly, hourly)

    Prevent overlapping runs

    Feature-flag scheduling (enable/disable via config)
 */


@Component
public class SpotifyEtlScheduler {
    private final SpotifyEtlJob etlJob;
    private final SpotifyEtlConfig config;

    // Variable used to prevent overlapping run
    private final AtomicBoolean running = new AtomicBoolean(false);

    public SpotifyEtlScheduler(SpotifyEtlJob etlJob, SpotifyEtlConfig config) {
        this.etlJob = etlJob;
        this.config = config;
    }

    @Scheduled(cron = "#{@spotifyEtlConfig.cron}")
    public void runScheduleEtl() {
        // Feature Flag
        if (!config.getSchedulingEnabled()) {
            System.out.println("[ETL] Scheduling disabled. Skipping run.");
            return;
        }

        // Overlap protection
        if (!running.compareAndSet(false, true)) {
            System.out.println("[ETL] Previous run still active. Skipping this schedule.");
            return;
        }

        try {
            System.out.println("[ETL] Scheduled run started.");
            etlJob.runEtl();
            System.out.println("[ETL] Scheduled run completed.");
        } catch (Exception e) {
            System.err.println("[ETL] Scheduled run failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            running.set(false);
        }
    }
}
