package com.erindavide

import org.telegram.telegrambots.TelegramBotsApi
import org.telegram.telegrambots.exceptions.TelegramApiException
import org.telegram.telegrambots.logging.BotLogger

val LOGTAG = "Tag_Main"

fun main(args: Array<String>){
    val telegramBotsApi = TelegramBotsApi()
    try {
        telegramBotsApi.registerBot(RssHandler())
    } catch (e: TelegramApiException) {
        BotLogger.error(LOGTAG, e)
    }

}

