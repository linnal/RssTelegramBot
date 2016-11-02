package com.erindavide

import com.erindavide.db.Storage
import com.heroku.sdk.jdbc.DatabaseUrl
import org.telegram.telegrambots.TelegramBotsApi
import org.telegram.telegrambots.exceptions.TelegramApiException
import org.telegram.telegrambots.logging.BotLogger
import java.sql.Connection
import java.sql.SQLException
import java.util.*

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
                var lastPublishedItem = Storage.getFeed(url)
                for(user in users){
                    handler.sendMessageTo(user.chatid, lastPublishedItem.toString())
                }
            }
        }

    } catch (e: TelegramApiException) {
        BotLogger.error(LOGTAG, e)
    }

    dbConnect()

}


fun dbConnect() {
    var connection: Connection? = null
    val attributes = HashMap<String, Any>()
    try {
        connection = DatabaseUrl.extract(true).connection

        val stmt = connection.createStatement()
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())")
        val rs = stmt.executeQuery("SELECT tick FROM ticks")

        val output = ArrayList<String>()
        while (rs.next()) {
            print( "===== Read from DB: " + rs.getTimestamp("tick"))
            output.add( "===== Read from DB: " + rs.getTimestamp("tick"))
        }

        attributes.put("results", output)
    } catch ( e: Exception) {
        print("There was an error: " + e)
    } finally {
        if (connection != null) try{connection.close()} catch( e: SQLException){}
    }
}

