package com.erindavide

import com.erindavide.db.Storage
import org.telegram.telegrambots.TelegramBotsApi
import org.telegram.telegrambots.exceptions.TelegramApiException
import org.telegram.telegrambots.logging.BotLogger
import java.net.ServerSocket
import java.net.SocketTimeoutException
import java.nio.charset.Charset

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

    val PORT = Integer.parseInt( System.getenv("PORT") ?: "3000" )

    val socket = ServerSocket(PORT)
    socket.soTimeout = 10000
    while(true){
        try {
            val conn = socket.accept()
            print(" === socket connection === $PORT")
            conn.outputStream.write("200 OK\n".toByteArray(Charset.defaultCharset()))
            conn.outputStream.flush()
            conn.outputStream.close()
            conn.close()
        }catch (e: SocketTimeoutException){
            println("DEBUG ${e.message}")
        }
    }

}


