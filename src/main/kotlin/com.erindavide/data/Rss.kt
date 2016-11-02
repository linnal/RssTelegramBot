package com.erindavide.data

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by linnal on 11/1/16.
 */
@XmlRootElement(name="rss")
@XmlAccessorType(XmlAccessType.FIELD)
class Rss(){
    @XmlElement(name = "channel")
    val channel = Channel()
}

@XmlAccessorType(XmlAccessType.FIELD)
class Channel{
    val title: String = ""
    val lastBuildDate: String = ""
    val pubDate: String = ""
    val link: String = ""
    @XmlElement(name = "item")
    val items = emptyList<Item>().toMutableList()
}