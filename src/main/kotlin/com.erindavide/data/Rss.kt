package com.erindavide.data

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name="rss")
@XmlAccessorType(XmlAccessType.FIELD)
class Rss(){
    @XmlElement(name = "channel")
    val channel = Channel()
}
