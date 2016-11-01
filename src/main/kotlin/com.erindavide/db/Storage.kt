package com.erindavide.db

import java.util.*

/**
 * Created by linnal on 10/31/16.
 */
object Storage {

    val dataContainer: MutableMap<Int, MutableSet<String>> = HashMap()

    fun addRss(userId: Int, rss: String): Boolean{
        val set = dataContainer.getOrPut(userId){ emptySet<String>().toMutableSet() }
        set.add(rss)

        return true
    }

    fun getAllRss(userId: Int): Set<String> {
        return dataContainer.getOrElse(userId){ emptySet<String>().toMutableSet() }
    }

    fun deleteRss(userId: Int, rssPosition: Int){

    }
}