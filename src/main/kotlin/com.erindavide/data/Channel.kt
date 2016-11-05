package com.erindavide.data

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement


@XmlAccessorType(XmlAccessType.FIELD)
class Channel{
    var title: String = ""
    var link: String = ""
    @XmlElement(name = "item")
    val items = emptyList<Item>().toMutableList()
}