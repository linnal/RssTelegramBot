package com.erindavide

import com.erindavide.db.Storage
import com.erindavide.parser.RssFeedParser

/**
 * Created by linnal on 11/1/16.
 */
object Poller {

    fun checkForUpdates(): List<String>{
        val result = emptyList<String>().toMutableList()

        val allFeeds = Storage.getAllRss()
        for(url in allFeeds){
            val rss = RssFeedParser.parseFeed(url)
            val existingRss = Storage.getFeed(url)
            val publishedRss = rss?.channel?.items?.first()?.link

            if(existingRss == null || existingRss.pubDate.isNullOrEmpty()){
                Storage.updateRss(rss!!)
                continue
            }

            if(! update_available(publishedRss, existingRss!!.link!!)){
                continue
            }

            Storage.updateRss(rss!!)
            result.add(url)
        }

        return result
    }

    private fun update_available(publishedLink: String?, existingLink: String): Boolean {
        return ! publishedLink.equals(existingLink)
    }

}