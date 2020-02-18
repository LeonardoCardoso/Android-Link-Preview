package com.leocardz.linkpreview.sample.library;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * A TextCrawler that allows you to provide the raw HTML that you would like to use for testing.
 */
public class ProvidedDocumentTextCrawler extends TextCrawler {
    private final String html;

    public ProvidedDocumentTextCrawler(final String html) {
        this.html = html;
    }

    @Override
    protected AsyncTaskImpl createPreviewGenerator(ImagePickingStrategy imagePickingStrategy) {
        return new AsyncTaskImpl(imagePickingStrategy, null) {

            @Override
            protected Document getDocument() throws IOException {
                return Jsoup.parse(html);
            }
        };
    }
}
