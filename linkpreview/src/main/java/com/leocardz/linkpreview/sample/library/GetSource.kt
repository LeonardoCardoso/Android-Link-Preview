package com.leocardz.linkpreview.sample.library

/**
 * @author dhruvaraj nagarajan
 */
interface GetSource {

    fun getSourceCode(url: String): SourceContent

    fun isNull(sourceContent: SourceContent): Boolean

    fun cancel()

    fun isCallCancelled(): Boolean
}
