package com.leocardz.link.preview.library;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ImagePickingStrategy that mimics the original behavior of TextCrawler.  If a meta image tag exists,
 * it is used, if not, the document is scraped to find other images.
 */
class DefaultImagePickingStrategy extends BaseImagePickingStrategy {

    /**
     * Gets images from the html code
     */
    @Override
    public List<String> getImages(AsyncTask asyncTask, Document document, HashMap<String, String> metaTags) {
        List<String> images = new ArrayList<>();
        final String metaImage = metaTags.get("image");

        if (!metaImage.equals("")) {
            images.add(metaImage);
        } else {
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
        }
        return images;
    }
}
