package com.erindavide.parser

import com.erindavide.data.Rss
import java.net.MalformedURLException
import java.net.URL
import javax.xml.bind.JAXBContext
import javax.xml.bind.UnmarshalException

/**
 * Created by linnal on 11/1/16.
 */
object RssFeedParser {

    fun parseFeed(url: String): Rss?{

        try {
            val _url = URL(url)
            val jc = JAXBContext.newInstance(Rss::class.java)

            val unmarshaller = jc.createUnmarshaller()
            return unmarshaller.unmarshal(_url) as Rss

        }catch(e: UnmarshalException){
            // TODO delete url
            return null
        }catch(e: MalformedURLException){
            // TODO delete url
            return null
        }

    }

}
