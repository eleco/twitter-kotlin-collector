package com.megalit.service

import java.util.stream.Collectors


data class Message(val url: String, val title: String, val screenName: String) {

    companion object {
        fun toHtml(msg: Set<Message>): String {
            return msg.stream()
                    .map { m -> "<p> <a href=" + m.url + ">" + m.title + "</a> via " + m.screenName + "</p>" }
                    .collect(Collectors.joining(System.lineSeparator()))
        }
    }

}



