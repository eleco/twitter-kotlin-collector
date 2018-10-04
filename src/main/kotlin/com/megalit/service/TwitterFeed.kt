package com.megalit.service


import com.google.common.base.Throwables
import com.google.common.collect.Iterables
import com.sendgrid.Content
import com.twitter.twittertext.Extractor
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import twitter4j.Paging
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS
import java.util.logging.Logger
import twitter4j.TwitterFactory
import twitter4j.Twitter
import twitter4j.auth.AccessToken


@Component
class TwitterFeed @Autowired constructor(
        val config: ConfigProperties
) {

    val logger = Logger.getLogger(TwitterFeed::class.java.name)

    lateinit var mailer: Mailer

    lateinit var twitter:Twitter

    //keep track of the maximum twitter id retrieved so far
    var sinceId: Long = 1

    //set of messages to email, cleared when email is sent
    val messagesToEmail = HashSet<Message>()

    //set of messages emailed, to reduce number of duplicate messages sent over time
    val messagesEmailed = HashSet<Message>()

    //max size of the messages emailed set
    val MAX_MESSAGES_EMAILED = 1000;

    //number of messages per email
    val MESSAGE_SEND_THRESHOLD = 10


    init {

        mailer = Mailer(config.sendgridfrom, config.sendgridto, config.sendgridid)
        mailer.doSendMail("twitter link emailer starting up", Content("text/html", "n/a"))


        twitter = TwitterFactory.getSingleton()
        twitter.setOAuthConsumer(config.consumerkey, config.consumersecret)
        twitter.setOAuthAccessToken(AccessToken(config.token, config.tokensecret))

    }

    @Scheduled(fixedRate = 10 * 60 * 1000)
    fun fetchTimeline() {

        logger.info("------------------------------------------");
        logger.info("fetching home timeline since id:" + sinceId)
        try {
            twitter.getHomeTimeline(Paging().sinceId(sinceId)).stream().forEach { t->

                    logger.info("${t.user.name} @${t.user.screenName}\n${t.text}")
                    process(t.text, t.user.screenName)
                    sinceId = Math.max(t.id, sinceId)

            }
        }catch (t: Throwable) {
            logger.severe(t.message + " -> "+ Throwables.getStackTraceAsString(t) );
        }
    }

    fun process(text: String, screenName: String) {

        Extractor().extractURLs(text).stream().forEach { url ->
            getDocumentOverHttp(url)?.let { doc ->
                val title = doc.title();
                val m = Message(url, title, screenName)

                if (!title.contains("on Twitter:") && !title.contains("Twitter. It's what's happening")
                        && !messagesEmailed.contains(m)) {

                    synchronized(this) {
                        messagesToEmail.add(m);
                        logger.info(" new message: " + m + " messagesToEmail size:" + messagesToEmail.size + " threshold:" + MESSAGE_SEND_THRESHOLD);
                        if (messagesToEmail.size >= MESSAGE_SEND_THRESHOLD) {

                            sendEmail()
                        }
                    }
                }
            }
        }
    }

    fun sendEmail() {
        mailer.sendMail(messagesToEmail);

        messagesEmailed.addAll(messagesToEmail);

        if (!messagesEmailed.isEmpty() && messagesEmailed.size >= MAX_MESSAGES_EMAILED) {
            messagesEmailed.remove(Iterables.get(messagesEmailed, 0));
        }

        messagesToEmail.clear()
    };

    fun getDocumentOverHttp(url: String): Document? {
        try {
            val response = Jsoup.connect(url).timeout(5000).ignoreHttpErrors(false).execute()
            if (response.statusCode() < 200 || response.statusCode() > 299) {
                logger.warning("unable to access:" + url + " -> status code: " + response.statusCode())
                return null
            } else {
                return response.parse()
            }
        } catch (e: Exception) {
            logger.warning("unable to access:" + url + " : " + e.message )
            return null
        }
    }
}






