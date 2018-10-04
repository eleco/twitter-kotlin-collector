package com.megalit


import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class Main : SpringBootServletInitializer() {

}

fun main(args: Array<String>) {
    SpringApplication.run(Main::class.java, *args)
}