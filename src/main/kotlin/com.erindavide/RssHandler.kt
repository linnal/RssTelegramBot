package com.erindavide

import com.erindavide.data.User
import com.erindavide.parser.TelegramUpdateParser
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot

class RssHandler() : TelegramLongPollingBot() {

    override fun getBotToken() = "277118030:AAEjBgC99F5jDJcFLS6oKYtdmHpw3o4iWYk"
    override fun getBotUsername() = "linrssbot"

    override fun onUpdateReceived(update: Update) {
        val userId = update.message.from.id
        val text = update.message.text
        val chatId = update.message.chatId.toString()

        val respose = TelegramUpdateParser.parseUserMessage( User(userId, chatId), text )
        sendMessageTo(chatId, respose)

    }


    fun sendMessageTo(chatId: String, message:String){
        val send = SendMessage()
        send.chatId = chatId
        send.setText(message)
        sendMessage(send)
    }


}