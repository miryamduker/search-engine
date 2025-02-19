package com.handson.searchengine.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.handson.searchengine.model.*;
import com.handson.searchengine.service.ElasticsearchService;
import com.handson.searchengine.service.KafkaService;
import com.handson.searchengine.service.RedisService;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Service
public class Crawler {
    private static final Logger logger = Logger.getLogger(Crawler.class.getName());
    private final WebPageFetcher webPageFetcher;
    private final LinkExtractor linkExtractor;
    private final RedisService redisService;
    private final ElasticsearchService elasticsearchService;
    private final KafkaService kafkaService;

    public Crawler(WebPageFetcher webPageFetcher, LinkExtractor linkExtractor, RedisService redisService,
                   ElasticsearchService elasticsearchIndexer, KafkaService kafkaService) {
        this.webPageFetcher = webPageFetcher;
        this.linkExtractor = linkExtractor;
        this.redisService = redisService;
        this.elasticsearchService = elasticsearchIndexer;
        this.kafkaService = kafkaService;
    }

    public void crawl(String crawlId, CrawlerRequest request) throws JsonProcessingException {
        redisService.setCrawlStatus(crawlId, CrawlStatus.of(0, System.currentTimeMillis(), 0, null));
        kafkaService.sendMessage(CrawlerRecord.of(crawlId, request));
    }

    public void crawlOneRecord(CrawlerRecord record) throws IOException {
        logger.info("Crawling URL: " + record.getUrl());

        StopReason stopReason = getStopReason(record);
        redisService.setCrawlStatus(record.getCrawlId(), CrawlStatus.of(record.getDistance(), record.getStartTime(), 0, stopReason));

        if (stopReason == null) {
            Document webPageContent = webPageFetcher.fetchPage(record.getUrl());
            elasticsearchService.indexElasticSearch(record, webPageContent);
            List<String> extractedLinks = linkExtractor.extractLinks(record.getBaseUrl(), webPageContent);
            addUrlsToQueue(record, extractedLinks, record.getDistance() + 1);
        }
    }

    private StopReason getStopReason(CrawlerRecord rec) {
        if (rec.getDistance() >= rec.getMaxDistance()) return StopReason.maxDistance;
        if (redisService.getVisitedCount(rec.getCrawlId()) >= rec.getMaxUrls()) return StopReason.maxUrls;
        if (System.currentTimeMillis() >= rec.getMaxTime()) return StopReason.timeout;
        return null;
    }

    private void addUrlsToQueue(CrawlerRecord rec, List<String> urls, int distance) throws JsonProcessingException {
        logger.info("Adding URLs to queue: distance=" + distance + " amount=" + urls.size());
        for (String url : urls) {
            if (!redisService.markVisited(rec, url)) {
                kafkaService.sendMessage(CrawlerRecord.of(rec).withUrl(url).withIncDistance());
            }
        }
    }
}
