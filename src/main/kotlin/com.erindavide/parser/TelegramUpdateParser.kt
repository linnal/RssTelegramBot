package com.erindavide.parser

import com.erindavide.data.User
import com.erindavide.db.Storage

/**
 * Created by linnal on 11/1/16.
 */
object TelegramUpdateParser {
    fun parseUserMessage(user: User, text: String): String{

        if(text.equals("/start") or text.equals("start") or text.equals("help")){
            return startMessage();
        }

        if(text.startsWith("http")){
            Storage.addRss(user, text)
        }

        return Storage.getAllRssFor(user.id).toString()
    }

    private fun startMessage(): String {
        return "To add an rss just write the rss url you want to get notifications \n"+
                "list to see the list of rss you are registered \n" +
                "delete #rss_nr taken from your rss list to delete a single rss\n" +
                "delte all to delete all the rss"
    }
}