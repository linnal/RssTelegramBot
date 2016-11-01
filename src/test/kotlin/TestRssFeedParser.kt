
import com.erindavide.db.Storage
import com.erindavide.parser.RssFeedParser
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by linnal on 11/1/16.
 */
class TestRssFeedParser {

    @Before
    fun clean() {
        Storage.deleteAll()
    }

    @Test
    fun testParseFeedWithWrongUrl(){
        val url = "https://github.com/aman-jham/Rss-Feeds-Reader-Android-Parse-XML"
        val rss= RssFeedParser.parseFeed(url)
        Assert.assertNull(rss)
    }

    @Test
    fun testParseFeedWithUnreachedUrl(){
        val url = "https://unreachedurl.com"
        val rss= RssFeedParser.parseFeed(url)
        Assert.assertNull(rss)
    }
}