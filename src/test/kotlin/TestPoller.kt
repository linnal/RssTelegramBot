
import com.erindavide.Poller
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
        Storage.addRss(1234, "http://linnal.github.io/feed.xml")
        val feeds = Poller.checkForUpdates()
        Assert.assertEquals(1, feeds.size)
    }

}