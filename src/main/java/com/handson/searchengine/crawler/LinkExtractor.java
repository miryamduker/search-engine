package com.handson.searchengine.crawler;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LinkExtractor {

    public List<String> extractLinks(String baseUrl, Document webPageContent) {
        return webPageContent.select("a[href]")  // No need to escape brackets
                .eachAttr("abs:href")
                .stream()
                .filter(url -> url.startsWith(baseUrl))
                .collect(Collectors.toList());
    }
}