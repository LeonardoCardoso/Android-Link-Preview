package com.leocardz.link.preview.library

import com.leocardz.link.preview.library.url.DefaultUrlExtractionStrategy
import com.leocardz.link.preview.library.url.UrlExtractionStrategy
import io.reactivex.Observable

/**
 * @author dhruvaraj nagarajan
 */
class GetSourceObservable(
        private val url: String,
        private val imagePickingStrategy: ImagePickingStrategy = DefaultImagePickingStrategy(),
        private val urlExtractionStrategy: UrlExtractionStrategy = DefaultUrlExtractionStrategy()
) : GetSource by GetSourceSync(imagePickingStrategy, urlExtractionStrategy) {

    fun getObservable() = Observable.create<SourceContent> { emitter ->
        val sourceContent = getSourceCode(url)

        if (!sourceContent.isSuccess) {
            emitter.onError(Exception("Content is null."))
            return@create
        }

        emitter.onNext(sourceContent)
        emitter.onComplete()
    }
}