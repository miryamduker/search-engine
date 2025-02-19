package com.handson.searchengine.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handson.searchengine.model.CrawlStatus;
import com.handson.searchengine.model.CrawlStatusOut;
import com.handson.searchengine.model.CrawlerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(RedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void setCrawlStatus(String crawlId, CrawlStatus crawlStatus) throws JsonProcessingException {
        redisTemplate.opsForValue().set(crawlId + ".status", objectMapper.writeValueAsString(crawlStatus));
    }

    public boolean markVisited(CrawlerRecord rec, String url) {
        if ( redisTemplate.opsForValue().setIfAbsent(rec.getCrawlId() + ".urls." + url, "1")) {
            redisTemplate.opsForValue().increment(rec.getCrawlId() + ".urls.count",1L);
            return false;
        } else {
            return true;
        }
    }

    public int getVisitedCount(String crawlId) {
        Object curCount = redisTemplate.opsForValue().get(crawlId + ".urls.count");
        if (curCount == null) return 0;
        return Integer.parseInt(curCount.toString());
    }

    public CrawlStatusOut getCrawlInfo(String crawlId) throws JsonProcessingException {
        CrawlStatus cs = objectMapper.readValue(redisTemplate.opsForValue().get(crawlId + ".status").toString(),CrawlStatus.class);
        cs.setNumPages(getVisitedCount(crawlId));
        return CrawlStatusOut.of(cs);
    }

}
