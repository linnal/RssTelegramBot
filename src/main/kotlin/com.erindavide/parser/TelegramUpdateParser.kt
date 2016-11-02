package com.erindavide.parser

import com.erindavide.data.User
import com.erindavide.db.Storage

/**
 * Created by linnal on 11/1/16.
 */
object TelegramUpdateParser {
    fun parseUserMessage(user: User, text: String): String{

        when(text){
            "/start", "/help" -> return startMessage()
            "/delete" -> return "TODO"
            else -> if(text.contains("http")) {
                Storage.addRss(user, text)
            }
        }
        return Storage.getAllRssFor(user.id).toString()
    }

    private fun startMessage(): String {
        return "Here you can follow your favorites rss feeds:\n\n" +
                "To add an rss just write the #rss_url you want to get notifications \n"+
                "/list - to see the list of rss you are registered \n" +
                "/delete #rss_nr - taken from your rss list to delete a single rss\n" +
                "/delte_all - to delete all the rss\n" +
                "/help - to see the list of possible commands"
    }
}