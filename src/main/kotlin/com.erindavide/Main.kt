package com.erindavide

import com.erindavide.db.DbStorage
import org.telegram.telegrambots.TelegramBotsApi
import org.telegram.telegrambots.exceptions.TelegramApiException
import org.telegram.telegrambots.logging.BotLogger

val LOGTAG = "Tag_Main"

fun main(args: Array<String>){

    val storage = DbStorage()
    storage.runMigrations()


    val telegramBotsApi = TelegramBotsApi()
    val handler = RssHandler()
    try {
        telegramBotsApi.registerBot(handler)

        kotlin.concurrent.timer("poller", false, 1000L, 10 * 60 * 1000L){
            println("checking")

            val rssList = Poller.checkForUpdates()
            for( url in rssList ){
                println("checking for $url")
                val users = storage.getAllUsersFor(url)
                var channelInfo = storage.getChannelInfo(url)
                for(user in users){
                    handler.sendMessageTo(user.chatid,
                            "Hey ${user.firstName}, \n " +
                            "${channelInfo.title} published: ${channelInfo.items.first().title}. \n " +
                            "Check it out ${channelInfo.items.first().link}")
                }
            }
        }

    } catch (e: TelegramApiException) {
        BotLogger.error(LOGTAG, e)
    }

    MySocket.create()
}


