package com.erindavide

import com.erindavide.db.Storage
import com.erindavide.parser.RssFeedParser

object Poller {

    fun checkForUpdates(): List<String>{
        val result = emptyList<String>().toMutableList()

        val allFeeds = Storage.getAllRss()
        for(url in allFeeds){
            val rss = RssFeedParser.parseFeed(url)

            val lastItemSaved = Storage.getFeed(url)
            val publishedItem = rss?.channel?.items?.first()

            if(! update_available(publishedItem?.link, lastItemSaved?.link)){
                continue
            }

            Storage.updateFeedItem(url, rss!!)
            result.add(url)
        }

        return result
    }

    private fun update_available(publishedLink: String?, existingLink: String?): Boolean {
        return ! publishedLink.equals(existingLink)
    }

}