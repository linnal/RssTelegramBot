
import com.erindavide.Poller
import com.erindavide.data.User
import com.erindavide.db.Storage
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by linnal on 11/1/16.
 */
class TestPoller {

    @Before
    fun clean() {
        Storage.deleteAll()
    }

    @Test
    fun testCheckForUpdates(){
        val url = "http://linnal.github.io/feed.xml"
        Storage.addRss(User(1234, "11"), url)
        val rssList = Poller.checkForUpdates()

        Assert.assertEquals(0, rssList.size)
    }

}