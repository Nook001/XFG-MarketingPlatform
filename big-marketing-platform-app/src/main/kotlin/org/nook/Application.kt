package org.nook

import org.springframework.beans.factory.annotation.Configurable
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Spring Boot 应用启动类
 */
@SpringBootApplication
@Configurable
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
