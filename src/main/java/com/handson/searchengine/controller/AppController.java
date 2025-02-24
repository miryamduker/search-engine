package com.handson.searchengine.controller;


import com.handson.searchengine.crawler.Crawler;
import com.handson.searchengine.kafka.Producer;
import com.handson.searchengine.model.CrawlStatusOut;
import com.handson.searchengine.model.CrawlerRequest;
import com.handson.searchengine.service.RedisService;
import com.handson.searchengine.util.CrawlIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping("/")
public class AppController {

    private static final int ID_LENGTH = 6;
    private Random random = new Random();
    @Autowired
    Crawler crawler;

    @Autowired
    Producer producer;

    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/crawl", method = RequestMethod.POST)
    public String crawl(@RequestBody CrawlerRequest request) throws IOException, InterruptedException {
        String crawlId = CrawlIdGenerator.generate();
        if (!request.getUrl().startsWith("http")) {
            request.setUrl("https://" + request.getUrl());
        }
        new Thread(()-> {
            try {
                crawler.crawl(crawlId, request);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return crawlId;
    }

    @RequestMapping(value = "/crawl/{crawlId}", method = RequestMethod.GET)
    public CrawlStatusOut getCrawl(@PathVariable String crawlId) throws IOException, InterruptedException {
        return redisService.getCrawlInfo(crawlId);
    }
}
