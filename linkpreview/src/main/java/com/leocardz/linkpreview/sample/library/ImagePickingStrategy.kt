package com.leocardz.linkpreview.sample.library

import org.jsoup.nodes.Document
import java.util.*

/**
 * A strategy for how to select the images to return.
 */
interface ImagePickingStrategy {

    var imageQuantity: Int

    fun getImages(getSource: GetSource, doc: Document, metaTags: HashMap<String, String>): List<String>
}
