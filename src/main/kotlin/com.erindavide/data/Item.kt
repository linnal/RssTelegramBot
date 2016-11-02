package com.erindavide.data

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by linnal on 11/1/16.
 */
@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
class Item(){

    @XmlElement(name="title")
    var title: String? = null

    @XmlElement(name="link")
    var link: String? = null

    @XmlElement(name="pubDate")
    var pubDate: String? = null

    override fun toString() = "title: $title link: $link pubDate: $pubDate"
}