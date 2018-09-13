package com.leocardz.link.preview.library;

import android.support.test.runner.AndroidJUnit4;

import com.leocardz.link.preview.library.url.UrlExtractionStrategy;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Instrumentation tests for TextCrawler.
 */
@RunWith(AndroidJUnit4.class)
public class TextCrawlerTest {
    @Test
    public void catastrophicJSoupErrorMarksSourceContentAsFailed() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);
        final JSoupFailingTextCrawler textCrawler = new JSoupFailingTextCrawler();

        final TestLinkPreviewCallback callback = new TestLinkPreviewCallback(signal);
        textCrawler.makePreview(callback, "http://www.google.com");
        signal.await();

        assertNotNull(callback.sourceContent);
        assertFalse(callback.sourceContent.isSuccess());
        assertTrue(callback.isNull);
    }

    @Test
    public void urlExtractedOutsideOfTextCrawlerDoesNotGoThroughUrlExtractionRoutine() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);
        final TextCrawler textCrawler = new TextCrawler();

        final TestLinkPreviewCallback callback = new TestLinkPreviewCallback(signal);
        final UrlExtractionStrategy mockUrlExtractionStrategy = mock(UrlExtractionStrategy.class);
        textCrawler.setUrlExtractionStrategy(mockUrlExtractionStrategy);
        final String textPassedToTextCrawler = "this is some text that may or may not contain a URL";
        textCrawler.makePreview(callback, textPassedToTextCrawler);
        signal.await();

        verify(mockUrlExtractionStrategy).extractUrls(textPassedToTextCrawler);
    }

    @Test
    public void urlExhibitingInfiniteRedirectIssueCanBeProcessed() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);
        final TextCrawler textCrawler = new TextCrawler();

        final TestLinkPreviewCallback callback = new TestLinkPreviewCallback(signal);

        textCrawler.makePreview(callback, "https://medium.muz.li/ui-design-tips-for-iphone-x-2652b2b248ce");
        signal.await(5000, TimeUnit.MILLISECONDS);

        final SourceContent sourceContent = callback.sourceContent;
        assertNotNull(sourceContent);
        assertTrue(sourceContent.isSuccess());
        assertFalse(callback.isNull);
    }

    /**
     * A TextCrawler that mimics a catastrophic failure in JSoup.
     */
    private class JSoupFailingTextCrawler extends TextCrawler {

        @Override
        protected GetCode createPreviewGenerator(ImagePickingStrategy imagePickingStrategy) {
            return new GetCode(imagePickingStrategy, null) {

                @Override
                protected Document getDocument() throws IOException {
                    throw new Error("catastrophic JSoup error has occurred.");
                }
            };
        }
    }

}
