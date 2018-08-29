package com.leocardz.link.preview.library;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseImagePickingStrategy implements ImagePickingStrategy {
    private int imageQuantity = TextCrawler.ALL;

    @Override
    public void setImageQuantity(int imageQuantity) {
        this.imageQuantity = imageQuantity;
    }

    @Override
    public int getImageQuantity() {
        return imageQuantity;
    }

    protected List<String> getMetaImage(HashMap<String, String> metaTags) {
        List<String> images = new ArrayList<>();
        final String metaImage = metaTags.get("image");

        if (!metaImage.equals("")) {
            images.add(metaImage);
        }
        return images;
    }

    protected List<String> getImagesFromImgTags(AsyncTask asyncTask, Document document) {
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
}
