package com.erindavide

import com.erindavide.db.Storage
import org.telegram.telegrambots.TelegramBotsApi
import org.telegram.telegrambots.exceptions.TelegramApiException
import org.telegram.telegrambots.logging.BotLogger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
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

    val socket = ServerSocket()
    socket.bind(InetSocketAddress(InetAddress.getLocalHost(), PORT))


    while(true){
        println(" === before socket accept connection === $PORT")
        val conn = socket.accept()
        println(" === after socket accept socket connection === $PORT")
        while( conn.inputStream.available() > 0 ){
            conn.inputStream.read()
        }
        println(" === conn write === $PORT")

        conn.outputStream.write("200 OK\n".toByteArray(Charset.defaultCharset()))
        println(" === conn flush === $PORT")

        conn.outputStream.flush()
        println(" === conn out stream close === $PORT")
        conn.outputStream.close()
        println(" === conn  close === $PORT")
        conn.close()
    }

}


