package com.erindavide.parser

import com.erindavide.data.User
import com.erindavide.db.Storage

/**
 * Created by linnal on 11/1/16.
 */
object TelegramUpdateParser {
    fun parseUserMessage(user: User, text: String): String{
        val cmd = text.split(" ")
        when(cmd.first()){
            "/start", "/help" -> return startMessage()
            "/delete" -> {
                if(cmd.size == 2){
                    Storage.deleteRss(user.id, cmd[1])
                }else{
                    return "Missing url feed you want to delete."
                }
            }
            "/delete_all" -> Storage.deleteAll(user.id)
            else -> if(text.contains("http")) {
                Storage.addRss(user, text)
            }
        }

        return Storage.getAllRssFor(user.id).joinToString("\n")
    }

    private fun startMessage(): String {
        return "Here you can follow your favorites rss feeds:\n\n" +
                "To add an rss just write the #rss_url you want to get notifications \n"+
                "/list - list all rss feeds you are registered to \n" +
                "/delete #rss_url - remove notifications for a single rss\n" +
                "/delete_all - remove notifications for all the rss\n" +
                "/help - to see the list of possible commands"
    }
}