package com.leocardz.linkpreview.sample.library.url;

import com.leocardz.linkpreview.sample.library.SearchUrls;
import com.leocardz.linkpreview.sample.library.TextCrawler;

import java.util.List;

public class DefaultUrlExtractionStrategy implements UrlExtractionStrategy {

    @Override
    public List<String> extractUrls(String textPassedToTextCrawler) {
        // Don't forget the http:// or https://
        List<String> urls = SearchUrls.matches(textPassedToTextCrawler);

        if (urls.size() > 0) {
            String url = TextCrawler.Companion.extendedTrim(urls.get(0));
            urls.set(0, url);
        }
        return urls;
    }
}
