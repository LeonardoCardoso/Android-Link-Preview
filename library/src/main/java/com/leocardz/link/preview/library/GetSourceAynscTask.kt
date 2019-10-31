package com.leocardz.link.preview.library

import android.os.AsyncTask
import com.leocardz.link.preview.library.url.DefaultUrlExtractionStrategy
import com.leocardz.link.preview.library.url.UrlExtractionStrategy

/**
 * @author dhruvaraj nagarajan
 */
class GetSourceAynscTask(
        private val callback: LinkPreviewCallback,
        private val imagePickingStrategy: ImagePickingStrategy = DefaultImagePickingStrategy(),
        private val urlExtractionStrategy: UrlExtractionStrategy = DefaultUrlExtractionStrategy()
) : AsyncTask<String, Void, SourceContent>(), GetSource by GetSourceSync(imagePickingStrategy, urlExtractionStrategy) {

    override fun onPreExecute() {
        callback.onPre()
        super.onPreExecute()
    }

    override fun onPostExecute(result: SourceContent) {
        callback.onPos(result, isNull(result))
        super.onPostExecute(result)
    }

    override fun doInBackground(vararg params: String): SourceContent {
        val url = params[0]
        return getSourceCode(url)
    }
}