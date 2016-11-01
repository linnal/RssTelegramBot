
import com.erindavide.db.Storage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


/**
 * Created by linnal on 10/31/16.
 */

class TestStorageSpec {

    @Before
    fun clean() {
        Storage.deleteAll()
    }

    @Test
    fun testAddRss(){
        val userId = 1234
        val rss = "http://local.com"

        assertTrue(Storage.addRss(userId, rss))
    }

    @Test
    fun testGetAllRss(){
        val userId = 1234
        val first_rss = "http://first/local.com"
        val second_rss = "http://second/local.com"

        Storage.addRss(userId, first_rss)
        Storage.addRss(userId, second_rss)

        assertEquals(2, Storage.getAllRssFor(userId).size)
    }

}