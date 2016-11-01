package com.erindavide.parser

import com.erindavide.data.Rss
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import java.net.URL
import javax.xml.bind.JAXBContext

/**
 * Created by linnal on 11/1/16.
 */
class RssFeedParser(val url: String) {

    fun parse(): String {
        val res = url.httpGet().responseString{request, respose, result ->
            when (result) {
                is Result.Failure -> {
                    print( result.getAs<FuelError>().toString() )
                }
                is Result.Success -> {
//                    parseSuccess( result.getAs<String>().toString() )
                }
            }
        }
        return ""
    }

    fun parseSuccess(){
        val _url = URL(url)
        val jc = JAXBContext.newInstance(Rss::class.java)

        val unmarshaller = jc.createUnmarshaller()
        val rss = unmarshaller.unmarshal(_url) as Rss
        //TODO save rss data to db
    }

}
