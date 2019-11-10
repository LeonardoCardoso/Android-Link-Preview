package com.leocardz.linkpreview.sample.library

import io.reactivex.Observable

class TextCrawler {

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = """
            AsyncTask has been deprecated since Android 11.
            Use RxJava variant, makePreview(url) instead.
            """,
        replaceWith = ReplaceWith("makePreview(url)")
    )
    fun makePreview(callback: LinkPreviewCallback, url: String) {
        val imagePickingStrategy = DefaultImagePickingStrategy()
        makePreview(callback, url, imagePickingStrategy)
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = """
            AsyncTask has been deprecated since Android 11.
            Use RxJava variant, makePreview(url) instead.
            """,
        replaceWith = ReplaceWith("makePreview(url)")
    )
    fun makePreview(
        callback: LinkPreviewCallback,
        url: String,
        imageQuantity: Int
    ) {
        val imagePickingStrategy = DefaultImagePickingStrategy()
        imagePickingStrategy.imageQuantity = imageQuantity
        makePreview(callback, url, imagePickingStrategy)
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = """
            AsyncTask has been deprecated since Android 11.
            Use RxJava variant, makePreview(url) instead.
            """,
        replaceWith = ReplaceWith("makePreview(url)")
    )
    fun makePreview(
        callback: LinkPreviewCallback,
        url: String,
        imagePickingStrategy: ImagePickingStrategy
    ) {
        GetSourceAynscTask(callback, imagePickingStrategy)
            .getSourceCode(url)
    }

    fun makePreview(url: String): Observable<SourceContent> =
        GetSourceObservable(url).getObservable()

    companion object {
        /**
         * Removes extra spaces and trim the string
         */
        fun extendedTrim(content: String): String = Util.extendedTrim(content)
    }
}