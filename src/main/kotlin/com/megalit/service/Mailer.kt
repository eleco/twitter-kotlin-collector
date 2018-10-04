package com.megalit.service

import com.sendgrid.*
import java.io.IOException
import java.util.*
import java.util.logging.Logger

class Mailer(_from: String, _to: String, _sendgridId: String) {

    private val logger = Logger.getLogger(Mailer::class.java.name)

    private var sendgrid: SendGrid
    private var from: String
    private var to: String

    init {
        to = _to
        from = _from
        sendgrid = SendGrid(_sendgridId)
    }

    fun sendMail(messages: Set<Message>) {
        val subject = "jlink digest at " + Date()
        val content = Content("text/html", Message.toHtml(messages))
        doSendMail(subject, content)
    }

    fun doSendMail(subject: String, content: Content) {

        val mail = Mail(Email(from), subject, Email(to), content)
        val request = Request()

        try {
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()
            val response = sendgrid.api(request)
            logger.info("email sent:" + response.statusCode)

        } catch (ex: IOException) {
            logger.severe("unable to send email:" + ex.message)
        }
    }

}
