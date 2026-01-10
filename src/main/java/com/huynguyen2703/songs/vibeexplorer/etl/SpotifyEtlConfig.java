package com.huynguyen2703.songs.vibeexplorer.etl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/*
    Responsibility:

    Centralize ETL-related configuration

    TODOs:

    Define batch sizes

    Define max pages per run

    Define default search keywords / artists

    Externalize configs to application.yml
 */


@Configuration
@ConfigurationProperties(prefix = "spotify.etl")
public class SpotifyEtlConfig {
    private int batchSize = 50; // number of tracks per DB insert
    private int maxPages = 10;  // number of pages to fetch
    private String[] defaultKeywords = new String[]{"pop", "ballad", "hiphop", "r&b"};
    private String market = "VN";
    private boolean schedulingEnabled = true;
    private String cron = "0 0 0 * * 5";

    // getters & setters
    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }

    public int getMaxPages() { return maxPages; }
    public void setMaxPages(int maxPages) { this.maxPages = maxPages; }

    public String[] getDefaultKeywords() { return defaultKeywords; }
    public void setDefaultKeywords(String[] defaultKeywords) { this.defaultKeywords = defaultKeywords; }

    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }

    public boolean getSchedulingEnabled() { return schedulingEnabled; }
    public void setSchedulingEnabled(boolean schedulingEnabled) { this.schedulingEnabled = schedulingEnabled; }

    public String getCron() { return cron; }
    public void setCron(String cron) { this.cron = cron; }
}
