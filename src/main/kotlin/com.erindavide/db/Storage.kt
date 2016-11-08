package com.erindavide.db

import com.erindavide.data.Channel
import com.erindavide.data.Item
import com.erindavide.data.Rss
import com.erindavide.data.User
import com.erindavide.db.migrations.BotUser
import com.erindavide.db.migrations.BotUserFeed
import com.erindavide.db.migrations.Feed
import com.heroku.sdk.jdbc.DatabaseUrl
import java.sql.Connection

object Storage {

    fun withConnection(f: Connection.() -> Unit){
        val connection = DatabaseUrl.extract(true).connection
        connection.f()
        connection.close()
    }

    fun runMigrations(){
        withConnection {
            val stmt = createStatement()
            stmt.executeUpdate(BotUser.createTable())
            stmt.execute(Feed.createTable())
            stmt.execute(BotUserFeed.createTable())
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

        val getRssForUser = "SELECT ID_FEED FROM BOTUSERFEED WHERE ID_BOTUSER=?"

        val connection = DatabaseUrl.extract(true).connection
        val stmt = connection.prepareStatement(getRssForUser)
        stmt.setInt(1, userId)
        val result = stmt.executeQuery()
        while(result.next()){
            rssList.add(result.getString("ID_FEED"))
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

        val getRssForUser = "SELECT * FROM FEED WHERE URL=?"

        val connection = DatabaseUrl.extract(true).connection
        val stmt = connection.prepareStatement(getRssForUser)
        stmt.setString(1, url)
        val result = stmt.executeQuery()
        if(result.next()){
            item = Item()
            item.link = result.getString("URL_ITEM")
            item.title = result.getString("TITLE_ITEM")
        }
        connection.close()

        return item
    }

    fun getChannelInfo(url: String): Channel{
        var channel: Channel = Channel()

        val getRssForUser = "SELECT * FROM FEED WHERE URL=?"

        val connection = DatabaseUrl.extract(true).connection
        val stmt = connection.prepareStatement(getRssForUser)
        stmt.setString(1, url)
        val result = stmt.executeQuery()
        if(result.next()){
            channel.title = result.getString("TITLE")
            val item = Item()
            item.link = result.getString("URL_ITEM")
            item.title = result.getString("TITLE_ITEM")
            channel.items.add(item)
        }
        connection.close()

        return channel
    }

    fun updateFeedItem(url:String, rss: Rss){
        updateItem(url, rss.channel)
    }

    fun getAllUsersFor(url: String): List<User> {

        val userList = emptyList<User>().toMutableList()

        val getRssForUser = """SELECT ID_BOTUSER, CHAT_ID, FIRSTNAME
                             FROM BOTUSERFEED, BOTUSER
                             WHERE ID_FEED=? AND BOTUSERFEED.ID_BOTUSER = BOTUSER.ID;"""

        val connection = DatabaseUrl.extract(true).connection
        val stmt = connection.prepareStatement(getRssForUser)
        stmt.setString(1, url)
        val result = stmt.executeQuery()
        while(result.next()){
            userList.add(User( result.getInt("ID_BOTUSER"),
                              result.getString("CHAT_ID"),
                              result.getString("FIRSTNAME")))
        }
        connection.close()

        return userList
    }

    fun deleteRss(userId: Int, url: String){
        val deleteFeed = """DELETE FROM FEED
                          WHERE URL=?; """

        val deleteUserFeed = """DELETE FROM BOTUSERFEED
                                WHERE ID_FEED=? AND ID_BOTUSER=?; """
        withConnection {
            var stmt = prepareStatement(deleteFeed)
            stmt.setString(1, url)
            stmt.execute()

            stmt = prepareStatement(deleteUserFeed)
            stmt.setString(1, url)
            stmt.setInt(2, userId)
            stmt.execute()
        }
    }


    fun deleteAll(userId: Int) {
        withConnection {
            val stmt = prepareStatement("DELETE FROM BOTUSERFEED WHERE ID_BOTUSER=?")
            stmt.setInt(1, userId)
            stmt.execute()
        }
    }




    private fun addOrUpdateUser(user: User){
        withConnection {
            val getUser = "SELECT * FROM BOTUSER WHERE ID=?"

            var stmt = prepareStatement(getUser)
            stmt.setInt(1, user.id)
            val res = stmt.executeQuery()

            if( !res.next() ){
                val insertUser = "INSERT INTO BOTUSER VALUES ( ? ,? ,? );"
                stmt = prepareStatement(insertUser)
                stmt.setInt(1, user.id)
                stmt.setString(2, user.firstName)
                stmt.setString(3, user.chatid)
                stmt.execute()
            }
        }
    }

    private fun addOrUpdateFeed(feed: Channel){
        withConnection {
            val getFeed = "SELECT * FROM FEED WHERE URL=?"

            var stmt = prepareStatement(getFeed)
            stmt.setString(1, feed.link)
            val res = stmt.executeQuery()
            if( res.next() ) {
                val updateFeed = "UPDATE FEED " +
                        " SET TITLE=? " +
                        " WHERE URL=?";

                stmt = prepareStatement(updateFeed)
                stmt.setString(1, feed.title.chop(35))
                stmt.setString(2, feed.link)
                stmt.executeUpdate()

            }else{
                val insertFeed = "INSERT INTO FEED (URL, TITLE) VALUES ( ? , ?);"
                stmt = prepareStatement(insertFeed)
                stmt.setString(1, feed.link)
                stmt.setString(2, feed.title.chop(35))
                stmt.execute()
            }

        }
    }

    private fun addUserFeed(userId: Int, feedId: String){
        withConnection {
            val getFeed = "SELECT * FROM BOTUSERFEED WHERE ID_BOTUSER=? AND ID_FEED=?"
            val insertFeed = """INSERT INTO BOTUSERFEED (ID_BOTUSER, ID_FEED)
                                VALUES ( ? , ?);"""

            var stmt = prepareStatement(getFeed)
            stmt.setInt(1, userId)
            stmt.setString(2, feedId)


            var res = stmt.executeQuery()
            if(!res.next()){
                stmt = prepareStatement(insertFeed)
                stmt.setInt(1, userId)
                stmt.setString(2, feedId)

                stmt.execute()
            }
        }
    }

    private fun updateItem(url: String, channel: Channel){
        withConnection {
            val item = channel.items.first()
            val updateFeed = "UPDATE FEED " +
                    " SET TITLE_ITEM=?, URL_ITEM=?, TITLE=? " +
                    " WHERE URL=?";

            val stmt = prepareStatement(updateFeed)
            stmt.setString(1, item.title?.chop(35))
            stmt.setString(2, item.link)
            stmt.setString(3, channel.title.chop(35))
            stmt.setString(4, url)

            stmt.execute()
        }
    }


    private fun String.chop(dim: Int): String{
        if(this.length >= dim) {
            return this.substring(0, dim)
        }
        return this
    }
}
