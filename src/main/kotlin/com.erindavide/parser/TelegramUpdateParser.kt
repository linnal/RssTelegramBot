package com.erindavide.parser

import com.erindavide.data.User
import com.erindavide.interfaces.Storage

class TelegramUpdateParser(val storage: Storage) {

    fun parseUserMessage(user: User, text: String): String{
        val cmd = text.split(" ")
        when(cmd.first()){

            "/start", "/help" -> return startMessage()

            "/delete" -> {
                if(cmd.size == 2){
                    storage.deleteRss(user.id, cmd[1])
                }else{
                    return "Missing url feed you want to delete."
                }
            }

            "/delete_all" -> storage.deleteAll(user.id)

            else -> if(text.contains("http")) {
                        if (text.startsWith("http")) {
                            storage.addRss(user, text)
                        } else {
                            return "Rss URL is not valid"
                        }
                    }
        }

        val rssList = storage.getAllRssFor(user.id).joinToString("\n")
        return "Rss you are follwing: \n" + (if(rssList.length > 1) rssList else "Nothing found")
    }

    private fun startMessage() =  "Here you can follow your favorites rss feeds:\n\n" +
                "To add an rss just write the #rss_url you want to get notifications \n"+
                "/list - list all rss feeds you are registered to \n" +
                "/delete rss_url - remove notifications for a single rss\n" +
                "/delete_all - remove notifications for all the rss\n" +
                "/help - to see the list of possible commands" + "\n\n" +
                "This is still in Beta, feel free to contribute or report bugs here: https://github.com/linnal/RssTelegramBot"

}