package db

import com.erindavide.data.Channel
import com.erindavide.data.Item
import com.erindavide.data.Rss
import com.erindavide.data.User
import com.erindavide.interfaces.Storage

/**
 * Created by linnal on 11/9/16.
 */
class FakeStorage: Storage {
    val rssList = emptyList<String>().toMutableList()

    override fun addRss(user: User, rss: String) {
        rssList.add(rss)
    }

    override fun getAllRssFor(userId: Int): List<String> {
        return rssList
    }

    override fun getAllRss(): List<String> {
        return rssList
    }

    override fun getFeed(url: String): Item? {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getChannelInfo(url: String): Channel {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateFeedItem(url: String, rss: Rss) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllUsersFor(url: String): List<User> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRss(userId: Int, url: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll(userId: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}