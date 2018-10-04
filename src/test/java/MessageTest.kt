import com.megalit.service.Message
import org.junit.Test

import com.google.common.collect.Sets.newHashSet
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.Is
import org.junit.Assert.assertThat

class MessageTest {


    @Test
    fun testMessageOutput() {


        val m1 = Message("url1", "title1", "screen1")
        val m2 = Message("url1", "title1", "screen1")
        val m3 = Message("url2", "title2", "screen2")

        val msg = newHashSet(m1, m2, m3)

        val str = Message.toHtml(msg)


        assertThat(normalizeLineEnds(str), Is.`is`(equalTo(
                normalizeLineEnds("<p> <a href=url1>title1</a> via screen1</p>\n<p> <a href=url2>title2</a> via screen2</p>"))))
    }

    fun normalizeLineEnds(s:String):String {
        return s.replace("\r\n", "\n").replace('\r', '\n');
    }
}

