package com.leocardz.link.preview.library;

import android.os.AsyncTask;
import android.support.test.runner.AndroidJUnit4;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation tests for TextCrawler Image handling.
 */
@RunWith(AndroidJUnit4.class)
public class TextCrawlerImageTest {
    @Test
    public void documentWithMetaImageAndDefaultStrategyReturnsMetaImage() throws Throwable {
        final String expectedImageUrl = "http://www.example.com/expected.jpg";
        final String testHtml = "<!doctype html>\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "  <title>\n" +
                "  Threddies - Bulk Headbands, Scrunchies, Ponytail Elastics, Hair Ties &ndash; threddies\n" +
                "  </title>\n" +
                "<meta property=\"og:site_name\" content=\"threddies\">\n" +
                "<meta property=\"og:url\" content=\"https://threddies.com/\">\n" +
                "<meta property=\"og:title\" content=\"Threddies - Bulk Headbands, Scrunchies, Ponytail Elastics, Hair Ties\">\n" +
                "<meta property=\"og:type\" content=\"website\">\n" +
                "<meta property=\"og:description\" content=\"Knit headbands, ponytail holders, scrunchies, satin headbands, non-slip headbands, hair elastics, turban headbands and beyond - at bulk pack wholesale prices!\">\n" +
                "<meta property=\"og:image\" content=\"" + expectedImageUrl + "\">\n" +
                "<meta name=\"twitter:site\" content=\"@threddies\">\n" +
                "<meta name=\"twitter:card\" content=\"summary_large_image\">\n" +
                "<meta name=\"twitter:title\" content=\"Threddies - Bulk Headbands, Scrunchies, Ponytail Elastics, Hair Ties\">\n" +
                "<meta name=\"twitter:description\" content=\"Knit headbands, ponytail holders, scrunchies, satin headbands, non-slip headbands, hair elastics, turban headbands and beyond - at bulk pack wholesale prices!\">\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "              <img src=\"http://www.example.com/notexpected.jpg\"\n" +
                "                alt=\"Threddies: Peace, Love, Headbands\"\n" +
                "                itemprop=\"logo\"\n" +
                "                style=\"max-width:380px;\">\n" +
                "</body>\n" +
                "</html>\n";
        final TextCrawler textCrawler = new ProvidedDocumentTextCrawler(testHtml);
        final CountDownLatch signal = new CountDownLatch(1);
        final TestLinkPreviewCallback callback = new TestLinkPreviewCallback(signal);
        textCrawler.makePreview(callback, "https://www.example.com");
        signal.await();

        final SourceContent sourceContent = callback.sourceContent;
        assertNotNull(sourceContent);
        assertTrue(sourceContent.isSuccess());
        assertFalse(callback.isNull);
        final List<String> images = sourceContent.getImages();
        assertNotNull(images);
        assertEquals(1, images.size());
        assertEquals(expectedImageUrl, images.get(0));
    }

    @Test
    public void documentWithNoMetaImageAndDefaultStrategyReturnsImage() throws Throwable {
        final String expectedImageUrl = "http://www.example.com/expected.jpg";
        final String testHtml = "<!doctype html>\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "  <title>\n" +
                "  Threddies - Bulk Headbands, Scrunchies, Ponytail Elastics, Hair Ties &ndash; threddies\n" +
                "  </title>\n" +
                "<meta property=\"og:site_name\" content=\"threddies\">\n" +
                "<meta property=\"og:url\" content=\"https://threddies.com/\">\n" +
                "<meta property=\"og:title\" content=\"Threddies - Bulk Headbands, Scrunchies, Ponytail Elastics, Hair Ties\">\n" +
                "<meta property=\"og:type\" content=\"website\">\n" +
                "<meta property=\"og:description\" content=\"Knit headbands, ponytail holders, scrunchies, satin headbands, non-slip headbands, hair elastics, turban headbands and beyond - at bulk pack wholesale prices!\">\n" +
                "<meta name=\"twitter:site\" content=\"@threddies\">\n" +
                "<meta name=\"twitter:card\" content=\"summary_large_image\">\n" +
                "<meta name=\"twitter:title\" content=\"Threddies - Bulk Headbands, Scrunchies, Ponytail Elastics, Hair Ties\">\n" +
                "<meta name=\"twitter:description\" content=\"Knit headbands, ponytail holders, scrunchies, satin headbands, non-slip headbands, hair elastics, turban headbands and beyond - at bulk pack wholesale prices!\">\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "              <img src=\"" + expectedImageUrl + "\"\n" +
                "                alt=\"Threddies: Peace, Love, Headbands\"\n" +
                "                itemprop=\"logo\"\n" +
                "                style=\"max-width:380px;\">\n" +
                "</body>\n" +
                "</html>\n";
        final TextCrawler textCrawler = new ProvidedDocumentTextCrawler(testHtml);
        final CountDownLatch signal = new CountDownLatch(1);
        final TestLinkPreviewCallback callback = new TestLinkPreviewCallback(signal);
        textCrawler.makePreview(callback, "https://www.example.com");
        signal.await();

        final SourceContent sourceContent = callback.sourceContent;
        assertNotNull(sourceContent);
        assertTrue(sourceContent.isSuccess());
        assertFalse(callback.isNull);
        final List<String> images = sourceContent.getImages();
        assertNotNull(images);
        assertEquals(1, images.size());
        assertEquals(expectedImageUrl, images.get(0));
    }

    @Test
    public void documentWithMetaImageAndCustomStrategyReturnsImage() throws Throwable {
        final String expectedImageUrl = "http://www.example.com/expected.jpg";
        final String testHtml = "<!doctype html>\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "  <title>\n" +
                "  Threddies - Bulk Headbands, Scrunchies, Ponytail Elastics, Hair Ties &ndash; threddies\n" +
                "  </title>\n" +
                "<meta property=\"og:site_name\" content=\"threddies\">\n" +
                "<meta property=\"og:url\" content=\"https://threddies.com/\">\n" +
                "<meta property=\"og:title\" content=\"Threddies - Bulk Headbands, Scrunchies, Ponytail Elastics, Hair Ties\">\n" +
                "<meta property=\"og:type\" content=\"website\">\n" +
                "<meta property=\"og:description\" content=\"Knit headbands, ponytail holders, scrunchies, satin headbands, non-slip headbands, hair elastics, turban headbands and beyond - at bulk pack wholesale prices!\">\n" +
                "<meta property=\"og:image\" content=\"http://www.example.com/notexpected.jpg\">\n" +
                "<meta name=\"twitter:site\" content=\"@threddies\">\n" +
                "<meta name=\"twitter:card\" content=\"summary_large_image\">\n" +
                "<meta name=\"twitter:title\" content=\"Threddies - Bulk Headbands, Scrunchies, Ponytail Elastics, Hair Ties\">\n" +
                "<meta name=\"twitter:description\" content=\"Knit headbands, ponytail holders, scrunchies, satin headbands, non-slip headbands, hair elastics, turban headbands and beyond - at bulk pack wholesale prices!\">\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "              <img src=\"" + expectedImageUrl + "\"\n" +
                "                alt=\"Threddies: Peace, Love, Headbands\"\n" +
                "                itemprop=\"logo\"\n" +
                "                style=\"max-width:380px;\">\n" +
                "</body>\n" +
                "</html>\n";
        final TextCrawler textCrawler = new ProvidedDocumentTextCrawler(testHtml);
        final CountDownLatch signal = new CountDownLatch(1);
        final TestLinkPreviewCallback callback = new TestLinkPreviewCallback(signal);
        textCrawler.makePreview(callback, "https://www.example.com", new BaseImagePickingStrategy() {

            @Override
            public List<String> getImages(AsyncTask asyncTask, Document document, HashMap<String, String> metaTags) {
                List<String> images = new ArrayList<>();

                Elements media = document.select("[src]");

                for (Element srcElement : media) {
                    if (asyncTask.isCancelled()) {
                        break;
                    }
                    if (srcElement.tagName().equals("img")) {
                        images.add(srcElement.attr("abs:src"));
                        if (getImageQuantity() != TextCrawler.ALL && images.size() == getImageQuantity()) {
                            break;
                        }
                    }
                }
                return images;
            }
        });
        signal.await();

        final SourceContent sourceContent = callback.sourceContent;
        assertNotNull(sourceContent);
        assertTrue(sourceContent.isSuccess());
        assertFalse(callback.isNull);
        final List<String> images = sourceContent.getImages();
        assertNotNull(images);
        assertEquals(1, images.size());
        assertEquals(expectedImageUrl, images.get(0));
    }


    @Test
    public void directLinkToImageThatEndsInFileExtensionReturnsImage() throws Throwable {
        final String expectedImageUrl = "https://cdn.shopify.com/s/files/1/1400/5075/files/website_logo2_720x_6cdc3363-fec0-4d60-a5bd-5236869352bf_720x.png";

        final TextCrawler textCrawler = new TextCrawler();
        final CountDownLatch signal = new CountDownLatch(1);
        final TestLinkPreviewCallback callback = new TestLinkPreviewCallback(signal);
        textCrawler.makePreview(callback, expectedImageUrl);
        signal.await();

        final SourceContent sourceContent = callback.sourceContent;
        assertNotNull(sourceContent);
        assertTrue(sourceContent.isSuccess());
        assertFalse(callback.isNull);
        final List<String> images = sourceContent.getImages();
        assertNotNull(images);
        assertEquals(1, images.size());
        assertEquals(expectedImageUrl, images.get(0));
    }

    @Test
    public void directLinkToImageThatDoesNotEndInFileExtensionReturnsImage() throws Throwable {
        final String expectedImageUrl = "https://cdn.shopify.com/s/files/1/1400/5075/files/website_logo2_720x_6cdc3363-fec0-4d60-a5bd-5236869352bf_720x.png?v=1533741874";

        final TextCrawler textCrawler = new TextCrawler();
        final CountDownLatch signal = new CountDownLatch(1);
        final TestLinkPreviewCallback callback = new TestLinkPreviewCallback(signal);
        textCrawler.makePreview(callback, expectedImageUrl);
        signal.await();

        final SourceContent sourceContent = callback.sourceContent;
        assertNotNull(sourceContent);
        assertTrue(sourceContent.isSuccess());
        assertFalse(callback.isNull);
        final List<String> images = sourceContent.getImages();
        assertNotNull(images);
        assertEquals(1, images.size());
        assertEquals(expectedImageUrl, images.get(0));
    }
}
