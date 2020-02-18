package com.leocardz.linkpreview.sample.library

import org.jsoup.nodes.Document
import java.util.*

abstract class BaseImagePickingStrategy : ImagePickingStrategy {

    protected fun getMetaImage(metaTags: HashMap<String, String>): List<String> {
        val images = ArrayList<String>()
        val metaImage = metaTags["image"]!!
        if (metaImage != "") {
            images.add(metaImage)
        }
        return images
    }

    protected fun getImagesFromImgTags(getSource: GetSource, document: Document): List<String> {
        val images = ArrayList<String>()
        val media = document.select("[src]")

        for (srcElement in media) {
            if (getSource.isCallCancelled()) {
                break
            }
            if (srcElement.tagName() == "img") {
                images.add(srcElement.attr("abs:src"))
                if (imageQuantity != QUANTITY_ALL && images.size == imageQuantity) {
                    break
                }
            }
        }
        return images
    }

    companion object {

        var QUANTITY_ALL = -1
        var QUANTITY_NONE = -2
    }
}
