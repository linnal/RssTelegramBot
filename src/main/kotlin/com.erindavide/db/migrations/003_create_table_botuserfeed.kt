package com.erindavide.db.migrations

object BotUserFeed{

    fun createTable(): String{
        return """CREATE TABLE IF NOT EXISTS BOTUSERFEED
                    (ID SERIAL ,
                     ID_BOTUSER INT         NOT NULL,
                     ID_FEED varchar(45)    NOT NULL,

                     PRIMARY KEY (ID)  );"""
    }
}