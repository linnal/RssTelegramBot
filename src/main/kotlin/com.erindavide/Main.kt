package com.erindavide

import com.erindavide.db.Storage
import org.telegram.telegrambots.TelegramBotsApi
import org.telegram.telegrambots.exceptions.TelegramApiException
import org.telegram.telegrambots.logging.BotLogger

val LOGTAG = "Tag_Main"

fun main(args: Array<String>){
    val telegramBotsApi = TelegramBotsApi()
    val handler = RssHandler()
    try {
        telegramBotsApi.registerBot(handler)

        kotlin.concurrent.timer("poller", false, 1000L, 1000L){
            val rssList = Poller.checkForUpdates()
            for( url in rssList ){
                val users = Storage.getAllUsersFor(url)
                var channelInfo = Storage.getChannelInfo(url)
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

    Storage.init()

}


