package com.leocardz.linkpreview.sample.library

import com.leocardz.linkpreview.sample.library.Util.extendedTrim
import com.leocardz.linkpreview.sample.library.url.UrlExtractionStrategy
import org.jsoup.Jsoup
import org.jsoup.UnsupportedMimeTypeException
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.*

/**
 * Reads url result synchronously.
 *
 * Wrap this in your choice of async wrapper, like Rx or Coroutine.
 */
class GetSourceSync(
        private val imagePickingStrategy: ImagePickingStrategy,
        private val urlExtractionStrategy: UrlExtractionStrategy
) : GetSource {

    private val HTTP_PROTOCOL = "http://"
    private val HTTPS_PROTOCOL = "https://"

    private var _isCancelled = false

    override fun getSourceCode(url: String): SourceContent {
        var url = url
        val sourceContent = SourceContent()

        url = unshortenUrl(url)

        sourceContent.finalUrl = url
        var wasPreviewGenerationSuccessful = false
        if (url != "") {
            if (isImage(url) && !url.contains("dropbox")) {
                sourceContent.images.add(sourceContent.finalUrl)
                sourceContent.title = ""
                sourceContent.description = ""
                wasPreviewGenerationSuccessful = true
            } else {
                try {
                    val doc = getDocument(sourceContent)

                    sourceContent.htmlCode = extendedTrim(doc.toString())

                    val metaTags = getMetaTags(sourceContent.htmlCode)

                    sourceContent.metaTags = metaTags

                    sourceContent.title = metaTags["title"]
                    sourceContent.description = metaTags["description"]

                    if (sourceContent.title == "") {
                        val matchTitle = Regex.pregMatch(
                                sourceContent.htmlCode,
                                Regex.TITLE_PATTERN, 2)

                        if (matchTitle != "")
                            sourceContent.title = htmlDecode(matchTitle)
                    }

                    if (sourceContent.description == "")
                        sourceContent.description = crawlCode(sourceContent.htmlCode)

                    sourceContent.description = sourceContent.description.replace(Regex.SCRIPT_PATTERN.toRegex(), "")

                    if (imagePickingStrategy.imageQuantity != BaseImagePickingStrategy.QUANTITY_NONE) {
                        val images: List<String> = imagePickingStrategy.getImages(this, doc, metaTags)
                        sourceContent.images = images
                    }

                    wasPreviewGenerationSuccessful = true
                } catch (t: Throwable) {
                    if (t is UnsupportedMimeTypeException) {
                        val mimeType = t.mimeType
                        if (mimeType != null && mimeType.startsWith("image")) {
                            sourceContent.images.add(sourceContent.finalUrl)
                            sourceContent.title = ""
                            sourceContent.description = ""
                            wasPreviewGenerationSuccessful = true
                        }
                    }
                }
            }

            sourceContent.isSuccess = wasPreviewGenerationSuccessful
        }

        val finalLinkSet = sourceContent.finalUrl.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        sourceContent.url = finalLinkSet[0]
        sourceContent.canonicalUrl = cannonicalPage(sourceContent.finalUrl)
        sourceContent.description = stripTags(sourceContent.description)

        return sourceContent
    }

    /**
     * Verifies if the content could not be retrieved
     */
    override fun isNull(sourceContent: SourceContent): Boolean {
        return !sourceContent.isSuccess &&
                extendedTrim(sourceContent.htmlCode) == "" &&
                !isImage(sourceContent.finalUrl)
    }

    override fun cancel() {
        _isCancelled = true
    }

    override fun isCallCancelled(): Boolean = _isCancelled

    /**
     * Verifies if the url is an image
     */
    fun isImage(url: String): Boolean {
        return url.matches(Regex.IMAGE_PATTERN.toRegex())
    }

    @Throws(IOException::class)
    private fun getDocument(sourceContent: SourceContent): Document {
        return Jsoup.connect(sourceContent.finalUrl).userAgent("Mozilla").get()
    }

    /**
     * Gets content from a html tag
     */
    private fun getTagContent(tag: String, content: String): String {

        val pattern = "<$tag(.*?)>(.*?)</$tag>"
        var result = ""
        var currentMatch = ""

        val matches = Regex.pregMatchAll(content, pattern, 2)

        val matchesSize = matches.size
        for (i in 0 until matchesSize) {
            if (isCallCancelled()) {
                break
            }
            currentMatch = stripTags(matches[i])
            if (currentMatch.length >= 120) {
                result = extendedTrim(currentMatch)
                break
            }
        }

        if (result == "") {
            val matchFinal = Regex.pregMatch(content, pattern, 2)
            result = extendedTrim(matchFinal)
        }

        result = result.replace("&nbsp;".toRegex(), "")

        return htmlDecode(result)
    }

    /**
     * Transforms from html to normal string
     */
    private fun htmlDecode(content: String): String {
        return Jsoup.parse(content).text()
    }

    /**
     * Crawls the code looking for relevant information
     */
    private fun crawlCode(content: String): String {
        val resultSpan = getTagContent("span", content)
        val resultParagraph = getTagContent("p", content)
        val resultDiv = getTagContent("div", content)

        val result: String

        if (resultParagraph.length > resultSpan.length && resultParagraph.length >= resultDiv.length)
            result = resultParagraph
        else if (resultParagraph.length > resultSpan.length && resultParagraph.length < resultDiv.length)
            result = resultDiv
        else
            result = resultParagraph

        return htmlDecode(result)
    }

    /**
     * Returns the cannoncial url
     */
    private fun cannonicalPage(url: String): String {
        var url = url

        var cannonical = ""
        if (url.startsWith(HTTP_PROTOCOL)) {
            url = url.substring(HTTP_PROTOCOL.length)
        } else if (url.startsWith(HTTPS_PROTOCOL)) {
            url = url.substring(HTTPS_PROTOCOL.length)
        }

        val urlLength = url.length
        for (i in 0 until urlLength) {
            if (isCallCancelled()) {
                break
            }
            if (url[i] != '/')
                cannonical += url[i]
            else
                break
        }

        return cannonical

    }

    /**
     * Strips the tags from an element
     */
    private fun stripTags(content: String): String {
        return Jsoup.parse(content).text()
    }

    /**
     * Returns meta tags from html code
     */
    private fun getMetaTags(content: String): HashMap<String, String> {

        val metaTags = HashMap<String, String>()
        metaTags["url"] = ""
        metaTags["title"] = ""
        metaTags["description"] = ""
        metaTags["image"] = ""

        val matches = Regex.pregMatchAll(content,
                Regex.METATAG_PATTERN, 1)

        for (match in matches) {
            if (isCallCancelled()) {
                break
            }
            val lowerCase = match.toLowerCase()
            if (lowerCase.contains("property=\"og:url\"")
                    || lowerCase.contains("property='og:url'")
                    || lowerCase.contains("name=\"url\"")
                    || lowerCase.contains("name='url'"))
                updateMetaTag(metaTags, "url", separeMetaTagsContent(match))
            else if (lowerCase.contains("property=\"og:title\"")
                    || lowerCase.contains("property='og:title'")
                    || lowerCase.contains("name=\"title\"")
                    || lowerCase.contains("name='title'"))
                updateMetaTag(metaTags, "title", separeMetaTagsContent(match))
            else if (lowerCase
                            .contains("property=\"og:description\"")
                    || lowerCase
                            .contains("property='og:description'")
                    || lowerCase.contains("name=\"description\"")
                    || lowerCase.contains("name='description'"))
                updateMetaTag(metaTags, "description", separeMetaTagsContent(match))
            else if (lowerCase.contains("property=\"og:image\"")
                    || lowerCase.contains("property='og:image'")
                    || lowerCase.contains("name=\"image\"")
                    || lowerCase.contains("name='image'"))
                updateMetaTag(metaTags, "image", separeMetaTagsContent(match))
        }

        return metaTags
    }

    private fun updateMetaTag(metaTags: HashMap<String, String>, url: String, value: String?) {
        if (value != null && value.length > 0) {
            metaTags[url] = value
        }
    }

    /**
     * Gets content from metatag
     */
    private fun separeMetaTagsContent(content: String): String {
        val result = Regex.pregMatch(content, Regex.METATAG_CONTENT_PATTERN,
                1)
        return htmlDecode(result)
    }

    /**
     * Unshortens a short url
     */
    private fun unshortenUrl(originURL: String): String {
        if (!originURL.startsWith(HTTP_PROTOCOL) && !originURL.startsWith(HTTPS_PROTOCOL))
            return ""

        var urlConn = connectURL(originURL)
        urlConn!!.headerFields

        val finalUrl = urlConn.url

        urlConn = connectURL(finalUrl)
        urlConn!!.headerFields

        val shortURL = urlConn.url

        var finalResult = shortURL.toString()

        while (!shortURL.sameFile(finalUrl)) {
            var isEndlesslyRedirecting = false
            if (shortURL.host == finalUrl.host) {
                if (shortURL.path == finalUrl.path) {
                    isEndlesslyRedirecting = true
                }
            }
            if (isEndlesslyRedirecting) {
                break
            } else {
                finalResult = unshortenUrl(shortURL.toString())
            }
        }

        return finalResult
    }

    /**
     * Takes a valid url string and returns a URLConnection object for the url.
     */
    private fun connectURL(strURL: String): URLConnection? {
        var conn: URLConnection? = null
        try {
            val inputURL = URL(strURL)
            conn = connectURL(inputURL)
        } catch (e: MalformedURLException) {
            println("Please input a valid URL")
        }

        return conn
    }

    /**
     * Takes a valid url and returns a URLConnection object for the url.
     */
    private fun connectURL(inputURL: URL): URLConnection? {
        var conn: URLConnection? = null
        try {
            conn = inputURL.openConnection()
        } catch (ioe: IOException) {
            println("Can not connect to the URL")
        }

        return conn
    }
}
