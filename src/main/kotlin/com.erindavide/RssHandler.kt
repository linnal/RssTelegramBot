package com.erindavide

import com.erindavide.data.User
import com.erindavide.parser.TelegramUpdateParser
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot

class RssHandler() : TelegramLongPollingBot() {

    override fun getBotToken() = System.getenv("BOT_TOKEN")
    override fun getBotUsername() = System.getenv("BOT_NAME")

    override fun onUpdateReceived(update: Update) {
        val userId = update.message.from.id
        val text = update.message.text
        val chatId = update.message.chatId.toString()
        val firstName = update.message.from.firstName
        print(update.message.toString())

        val respose = TelegramUpdateParser.parseUserMessage( User(userId, chatId, firstName), text )
        sendMessageTo(chatId, respose)

    }


    fun sendMessageTo(chatId: String, message:String){
        val send = SendMessage()
        send.chatId = chatId
        send.text = message
        sendMessage(send)
    }


}