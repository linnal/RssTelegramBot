package com.erindavide

import com.erindavide.data.Item
import com.erindavide.db.Storage
import com.erindavide.parser.RssFeedParser
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by linnal on 11/1/16.
 */
object Poller {

    fun checkForUpdates(): List<Item> {
        val result = emptyList<Item>().toMutableList()
        val allFeeds = Storage.getAllRss()
        for(url in allFeeds){
            val rss = RssFeedParser.parseFeed(url)
            val date = rss?.channel?.lastBuildDate
            val existingRss = Storage.getFeed(url)

            if(! update_available(date, existingRss?.pubDate)){
                continue
            }
            result.add(rss!!.channel.items.get(0))
        }
        return result
    }

    private fun update_available(publishedDate: String?, existingDate: String?): Boolean {
        if(publishedDate.isNullOrEmpty()){
            return false
        }

        if(existingDate.isNullOrEmpty()){
            return true
        }

        val formater = DateTimeFormatter.RFC_1123_DATE_TIME
        val rssPublishedDate = LocalDate.parse(publishedDate, formater)
        val rssLastDateSaved = LocalDate.parse(existingDate, formater)

        val diffYear = rssPublishedDate.year - rssLastDateSaved.year
        val diffMonth = rssPublishedDate.monthValue - rssLastDateSaved.monthValue
        val diffDay = rssPublishedDate.dayOfMonth - rssLastDateSaved.dayOfMonth

        return diffYear >= 0 && diffMonth >= 0 && diffDay > 0
    }

}