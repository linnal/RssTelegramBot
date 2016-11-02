
import com.erindavide.data.User
import com.erindavide.db.Storage
import com.erindavide.parser.TelegramUpdateParser
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by linnal on 11/1/16.
 */
class TestTelegramUpdateParser{

    @Before
    fun clean() {
        Storage.deleteAll()
    }

    @Test
    fun testParseUserMessageWithMessageStart(){
        val response = TelegramUpdateParser.parseUserMessage(User(1234, "11"), "start")
        Assert.assertTrue(response.contains("To add an rss just write"))
    }

    @Test
    fun testParseUserMessageWithMessageAddRss(){
        TelegramUpdateParser.parseUserMessage(User(1234, "11"), "http://localhost")
        val response = TelegramUpdateParser.parseUserMessage(User(1234, "11"), "http://erinda")
        Assert.assertEquals("[http://localhost, http://erinda]", response)
    }

}