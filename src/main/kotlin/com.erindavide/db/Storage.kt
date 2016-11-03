package com.erindavide.db

import com.erindavide.data.Channel
import com.erindavide.data.Item
import com.erindavide.data.Rss
import com.erindavide.data.User
import com.heroku.sdk.jdbc.DatabaseUrl
import java.sql.Connection

/**
 * Created by linnal on 10/31/16.
 */
object Storage {


    fun withConnection(f: Connection.() -> Unit){
        val connection = DatabaseUrl.extract(true).connection
        connection.f()
        connection.close()
    }

    fun init(){
        withConnection {
            val stmt = createStatement()

            val create_table_user = """CREATE TABLE IF NOT EXISTS BOTUSER
                                    (ID integer      NOT NULL,
                                     FIRSTNAME varchar(45),
                                     CHAT_ID varchar(45)           NOT NULL,
                                     PRIMARY KEY (ID)  );"""
            stmt.executeUpdate(create_table_user)

            val create_table_feed = """CREATE TABLE IF NOT EXISTS FEED
                                    (URL varchar(45),
                                    TITLE varchar(200),
                                    URL_ITEM varchar(200),
                                    TITLE_ITEM varchar(45),
                                    PRIMARY KEY (URL)   );"""
            stmt.execute(create_table_feed)


            val create_table_userfeed = """CREATE TABLE IF NOT EXISTS BOTUSERFEED
                                        (ID SERIAL ,
                                         ID_BOTUSER INT NOT NULL,
                                         ID_FEED varchar(45) NOT NULL  ,
                                         PRIMARY KEY (ID)  );"""
            stmt.execute(create_table_userfeed)

        }
    }




    fun addRss(user: User, rss: String) {

        addOrUpdateUser(user)

        val channel = Channel()
        channel.link = rss
        addOrUpdateFeed(channel)

        addUserFeed(user.id, rss)
    }

    fun getAllRssFor(userId: Int): List<String> {
        val rssList = emptyList<String>().toMutableList()

        val getRssForUser = "SELECT ID_FEED FROM BOTUSERFEED WHERE ID_BOTUSER=$userId"

        val connection = DatabaseUrl.extract(true).connection
        val stmt = connection.createStatement()
        val result = stmt.executeQuery(getRssForUser)
        while(result.next()){
            rssList.add(result.getString("url"))
        }
        connection.close()

        return rssList
    }

    fun getAllRss(): List<String>{
        val rssList = emptyList<String>().toMutableList()

        val getRssForUser = "SELECT URL FROM FEED"

        val connection = DatabaseUrl.extract(true).connection
        val stmt = connection.createStatement()
        val result = stmt.executeQuery(getRssForUser)
        while(result.next()){
            rssList.add(result.getString("URL"))
        }
        connection.close()

        return rssList
    }

    fun getFeed(url: String): Item?{
        var item: Item? = null

        val getRssForUser = "SELECT * FROM FEED WHERE URL=$url"

        val connection = DatabaseUrl.extract(true).connection
        val stmt = connection.createStatement()
        val result = stmt.executeQuery(getRssForUser)
        if(result.next()){
            item = Item()
            item.link = result.getString("URL_ITEM")
            item.title = result.getString("TITLE_ITEM")
        }
        connection.close()

        return item
    }

    fun updateFeedItem(rss: Rss){
        val url = rss.channel.link + "feed.xml"
        val publishedItem = rss.channel.items.first()

        updateItem(url, publishedItem)
    }

    fun getAllUsersFor(url: String): List<User> {

        val userList = emptyList<User>().toMutableList()

        val getRssForUser = """SELECT ID_BOTUSER, CHAT_ID, FIRSTNAME
                             FROM BOTUSERFEED, BOTUSER
                             WHERE ID_FEED=$url AND BOTUSERFEED.ID_BOTUSER = BOTUSER.ID;"""

        val connection = DatabaseUrl.extract(true).connection
        val stmt = connection.createStatement()
        val result = stmt.executeQuery(getRssForUser)
        while(result.next()){
            userList.add(User( result.getInt("ID_BOTUSER"),
                              result.getString("CHAT_ID"),
                              result.getString("FIRSTNAME")))
        }
        connection.close()

        return userList
    }

    fun deleteRss(userId: Int, rssPosition: Int){ }

    fun deleteRss(url: String){
        val deleteFeed = """DELETE FROM FEED
                          WHERE URL=$url; """

        val deleteUserFeed = """DELETE FROM BOTUSERFEED
                                WHERE ID_FEED=$url; """
        withConnection {
            val stmt = createStatement()
            stmt.executeQuery(deleteFeed)
            stmt.executeQuery(deleteUserFeed)
        }
    }

    fun deleteAll() {
        withConnection {
            val stmt = createStatement()
            stmt.executeQuery("DELETE FROM FEED")
            stmt.executeQuery("DELETE FROM BOTUSERFEED")
        }
    }




    private fun addOrUpdateUser(user: User){
        withConnection {
            val stmt = createStatement()
            val insertUser = "INSERT INTO BOTUSER VALUES ( $user.id , '$user.firstName', $user.chatid);"

            stmt.executeQuery(insertUser)
        }
    }

    private fun addOrUpdateFeed(feed: Channel){
        withConnection {
            val stmt = createStatement()
            val updateFeed = "UPDATE FEED " +
                    " SET TITLE=${feed.title} " +
                    " WHERE URL=${feed.link}";
            val insertFeed = "INSERT INTO BOTUSER VALUES ( '${feed.link}' , '${feed.title}');"

            stmt.executeQuery(updateFeed)
            stmt.executeQuery(insertFeed)
        }
    }

    private fun addUserFeed(userId: Int, feedId: String){
        withConnection {
            val stmt = createStatement()
            val getFeed = "SELECT * FROM ITEM WHERE ID_BOTUSER=${userId} AND ID_FEED=${feedId}"
            val insertFeed = "INSERT INTO BOTUSERFEED " +
                    " VALUES ( null, ${userId} , '${feedId}');"

            if(!stmt.executeQuery(getFeed).first()){
                stmt.executeQuery(insertFeed)
            }
        }
    }

    private fun updateItem(feedId: String, item: Item){
        withConnection {
            val stmt = createStatement()
            val updateFeed = "UPDATE FEED " +
                    " SET TITLE_ITEM=${item.title}, URL_ITEM=${item.link} " +
                    " WHERE URL=${feedId}";

            stmt.executeQuery(updateFeed)
        }
    }

}
