package com.leocardz.linkpreview.sample.library;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;

/**
 * ImagePickingStrategy that mimics the original behavior of TextCrawler.  If a meta image tag exists,
 * it is used, if not, the document is scraped to find other images.
 */
class DefaultImagePickingStrategy extends BaseImagePickingStrategy {

    private int imageQuantity;

    public DefaultImagePickingStrategy() {
        this(Companion.getQUANTITY_ALL());
    }

    public DefaultImagePickingStrategy(int imageQuantity) {
        setImageQuantity(imageQuantity);
    }

    /**
     * Gets images from the html code
     */
    @Override
    public List<String> getImages(GetSource getSource, Document document, HashMap<String, String> metaTags) {
        List<String> images = getMetaImage(metaTags);
        if (images.isEmpty()) {
            images.addAll(getImagesFromImgTags(getSource, document));
        }
        return images;
    }

    @Override
    public int getImageQuantity() {
        return imageQuantity;
    }

    @Override
    public void setImageQuantity(int i) {
        imageQuantity = i;
    }
}
