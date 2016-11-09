package com.erindavide

import com.erindavide.db.DbStorage
import com.erindavide.parser.RssFeedParser

object Poller {

    val storage = DbStorage()

    fun checkForUpdates(): List<String>{
        val result = emptyList<String>().toMutableList()

        val allFeeds = storage.getAllRss()
        for(url in allFeeds){
            val rss = RssFeedParser.parseFeed(url)

            val lastItemSaved = storage.getFeed(url)
            println("lastItemSaved = ${lastItemSaved.toString()}")
            val publishedItem = rss?.channel?.items?.first()
            println("publishedItem = ${publishedItem.toString()}")

            if(! update_available(publishedItem?.link, lastItemSaved?.link)){
                continue
            }

            storage.updateFeedItem(url, rss!!)
            result.add(url)
        }

        return result
    }

    private fun update_available(publishedLink: String?, existingLink: String?): Boolean {
        return ! publishedLink.equals(existingLink)
    }

}