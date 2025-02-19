package com.handson.searchengine.service;

import com.handson.searchengine.model.CrawlerRecord;
import com.handson.searchengine.model.UrlSearchDoc;
import com.handson.searchengine.util.ElasticSearch;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {
    private final ElasticSearch elasticSearch;

    public ElasticsearchService(ElasticSearch elasticSearch) {
        this.elasticSearch = elasticSearch;
    }

    public void indexElasticSearch(CrawlerRecord rec, Document webPageContent) {
        String text = String.join(" ", webPageContent.select("a[href]").eachText());
        UrlSearchDoc searchDoc = UrlSearchDoc.of(rec.getCrawlId(), text, rec.getUrl(), rec.getBaseUrl(), rec.getDistance());
        elasticSearch.addData(searchDoc);
    }
}

