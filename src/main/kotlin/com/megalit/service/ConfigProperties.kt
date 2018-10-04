package com.megalit.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "collector")
data class ConfigProperties(
        var consumerkey: String = "",
        var consumersecret: String = "",
        var token: String = "",
        var tokensecret: String = "",
        var sendgridid: String = "",
        var sendgridto: String = "",
        var sendgridfrom: String = "") {

}


