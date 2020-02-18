package com.leocardz.linkpreview.sample.library

/**
 * @author dhruvaraj nagarajan
 */
object Util {

    /**
     * Removes extra spaces and trim the string
     */
    fun extendedTrim(content: String): String {
        return content.replace("\\s+".toRegex(), " ").replace("\n", " ")
                .replace("\r", " ").trim { it <= ' ' }
    }
}