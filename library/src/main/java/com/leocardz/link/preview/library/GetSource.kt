package com.leocardz.link.preview.library

/**
 * @author dhruvaraj nagarajan
 */
interface GetSource {

    fun getSourceCode(url: String): SourceContent

    fun isNull(sourceContent: SourceContent): Boolean

    fun cancel()

    fun isCallCancelled(): Boolean
}
