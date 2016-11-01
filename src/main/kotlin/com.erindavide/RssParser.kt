package com.erindavide

import com.erindavide.db.Storage

/**
 * Created by linnal on 11/1/16.
 */
object RssParser {
    fun parseUserMessage(userId: Int, text: String): String{

        if(text.equals("/start") or text.equals("start") or text.equals("help")){
            return startMessage();
        }

        if(text.startsWith("http")){
            Storage.addRss(userId, text)
        }

        return Storage.getAllRss(userId).toString()
    }

    private fun startMessage(): String {
        return "To add an rss just write the rss url you want to get notifications \n"+
                "list to see the list of rss you are registered \n" +
                "delete #rss_nr taken from your rss list to delete a single rss\n" +
                "delte all to delete all the rss"
    }
}