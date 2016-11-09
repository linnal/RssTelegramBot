package com.erindavide.interfaces

import com.erindavide.data.Channel
import com.erindavide.data.Item
import com.erindavide.data.Rss
import com.erindavide.data.User

/**
 * Created by linnal on 11/9/16.
 */
interface Storage {

    fun addRss(user: User, rss: String)
    fun getAllRssFor(userId: Int): List<String>
    fun getAllRss(): List<String>
    fun getFeed(url: String): Item?
    fun getChannelInfo(url: String): Channel
    fun updateFeedItem(url:String, rss: Rss)
    fun getAllUsersFor(url: String): List<User>
    fun deleteRss(userId: Int, url: String)
    fun deleteAll(userId: Int)


}