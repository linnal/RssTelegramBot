package com.erindavide.db

import com.erindavide.data.Item
import java.util.*

/**
 * Created by linnal on 10/31/16.
 */
object Storage {

    val userToFeeds: MutableMap<Int, MutableSet<String>> = HashMap()
    val feedToUsers: MutableMap<String, MutableSet<Int>> = HashMap()
    val feedItems: MutableMap<String, Item> = HashMap()


    fun addRss(userId: Int, rss: String): Boolean{
        val setFeeds = userToFeeds.getOrPut(userId){ emptySet<String>().toMutableSet() }
        setFeeds.add(rss)

        val setUsers = feedToUsers.getOrPut(rss){ emptySet<Int>().toMutableSet() }
        setUsers.add(userId)

        val i = Item()
        i.link = rss

        feedItems.put(rss, i)

        return true
    }

    fun getAllRssFor(userId: Int) = userToFeeds.getOrElse(userId){ emptySet<String>() }

    fun getAllRss() = feedToUsers.keys

    fun getFeed(url: String) = feedItems.get(url)

    fun deleteRss(userId: Int, rssPosition: Int){ }
    
    fun deleteAll() {
        userToFeeds.clear()
        feedToUsers.clear()
        feedItems.clear()
    }
}