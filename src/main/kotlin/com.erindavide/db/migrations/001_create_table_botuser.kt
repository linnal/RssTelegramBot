package com.erindavide.db.migrations

object BotUser{

    fun createTable(): String{
        return """CREATE TABLE IF NOT EXISTS BOTUSER
                    (ID integer             NOT NULL,
                     FIRSTNAME varchar(45),
                     CHAT_ID varchar(45)    NOT NULL,

                     PRIMARY KEY (ID)  );"""
    }
}