package com.erindavide.db.migrations

object Feed{

    fun createTable(): String{
        return """CREATE TABLE IF NOT EXISTS FEED
                    (URL varchar(200),
                    TITLE varchar(200),
                    URL_ITEM varchar(200),
                    TITLE_ITEM varchar(45),

                    PRIMARY KEY (URL)   );"""
    }
}