package com.leocardz.link.preview.library;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;

/**
 * A strategy for how to select the images to return.
 */
public interface ImagePickingStrategy {

    void setImageQuantity(int imageQuantity);

    int getImageQuantity();

    List<String> getImages(AsyncTask asyncTask, Document doc, HashMap<String, String> metaTags);
}
