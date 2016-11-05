package com.erindavide.parser

import com.erindavide.data.Rss
import java.net.MalformedURLException
import java.net.URL
import javax.xml.bind.JAXBContext
import javax.xml.bind.UnmarshalException

object RssFeedParser {

    fun parseFeed(url: String): Rss?{

        try {
            val _url = URL(url)
            val jc = JAXBContext.newInstance(Rss::class.java)

            val unmarshaller = jc.createUnmarshaller()
            return unmarshaller.unmarshal(_url) as Rss

        }catch(e: UnmarshalException){
            println("DEBUG: ${e.message}")
            return null
        }catch(e: MalformedURLException){
            println("DEBUG: ${e.message}")
            return null
        }

    }

}
