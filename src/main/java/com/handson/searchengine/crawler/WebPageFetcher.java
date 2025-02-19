package com.handson.searchengine.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebPageFetcher {
    public Document fetchPage(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
