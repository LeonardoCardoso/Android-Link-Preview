package com.leocardz.link.preview.library;

import android.support.test.runner.AndroidJUnit4;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    /**
     * A TextCrawler that mimics a catastrophic failure in JSoup.
     */
    private class JSoupFailingTextCrawler extends TextCrawler {

        @Override
        protected GetCode createPreviewGenerator(ImagePickingStrategy imagePickingStrategy) {
            return new GetCode(imagePickingStrategy) {

                @Override
                protected Document getDocument() throws IOException {
                    throw new Error("catastrophic JSoup error has occurred.");
                }
            };
        }
    }

}
