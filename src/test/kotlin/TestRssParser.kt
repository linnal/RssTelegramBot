
import com.erindavide.RssParser
import org.junit.Assert
import org.junit.Test

/**
 * Created by linnal on 11/1/16.
 */
class TestRssParser {

    @Test
    fun testParseUserMessage_with_message_start(){
        val response = RssParser.parseUserMessage(1234, "start")
        Assert.assertTrue(response.contains("To add an rss just write"))
    }

    @Test
    fun testParseUserMessage_with_message_add_rss(){
        RssParser.parseUserMessage(1234, "http://localhost")
        val response = RssParser.parseUserMessage(1234, "http://erinda")
        Assert.assertEquals("[http://localhost, http://erinda]", response)
    }

}