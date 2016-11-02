package com.erindavide.db

import com.erindavide.data.Item
import com.erindavide.data.Rss
import com.erindavide.data.User
import com.heroku.sdk.jdbc.DatabaseUrl
import java.sql.Connection
import java.sql.SQLException
import java.util.*

/**
 * Created by linnal on 10/31/16.
 */
object Storage {

    val userToFeeds: MutableMap<Int, MutableSet<String>> = HashMap()
    val feedToUsers: MutableMap<String, MutableSet<Int>> = HashMap()
    val feedItems: MutableMap<String, Item> = HashMap()
    val users: MutableMap<Int, User> = HashMap()


    fun addRss(user: User, rss: String): Boolean{
        val setFeeds = userToFeeds.getOrPut(user.id){ emptySet<String>().toMutableSet() }
        setFeeds.add(rss)

        val setUsers = feedToUsers.getOrPut(rss){ emptySet<Int>().toMutableSet() }
        setUsers.add(user.id)

        val i = Item()
        i.link = rss

        feedItems.put(rss, i)
        users.put(user.id, user)

        return true
    }

    fun getAllRssFor(userId: Int) = userToFeeds.getOrElse(userId){ emptySet<String>() }

    fun getAllRss() = feedToUsers.keys

    fun getFeed(url: String) = feedItems[url]

    fun updateRss(rss: Rss){
        val url = rss.channel.link + "feed.xml"
        feedItems[url] = rss.channel.items.first()
    }

    fun getAllUsersFor(url: String): List<User> {
        return feedToUsers[url]!!.map { users[it]!! }
    }

    fun deleteRss(userId: Int, rssPosition: Int){ }
    
    fun deleteAll() {
        userToFeeds.clear()
        feedToUsers.clear()
        feedItems.clear()
        users.clear()
    }

}
