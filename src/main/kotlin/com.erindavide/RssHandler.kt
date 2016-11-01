package com.erindavide

import com.erindavide.parser.TelegramUpdateParser
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot

class RssHandler() : TelegramLongPollingBot() {

    override fun getBotToken() = "277118030:AAEjBgC99F5jDJcFLS6oKYtdmHpw3o4iWYk"
    override fun getBotUsername() = "linrssbot"

    override fun onUpdateReceived(update: Update) {
        print(update.toString())

        val userId = update.message.from.id
        val text = update.message.text

        val respose = TelegramUpdateParser.parseUserMessage(userId, text)

        val send = SendMessage()
        send.chatId = update.message.chatId.toString()
        send.setText(respose)
        sendMessage(send)
    }



}