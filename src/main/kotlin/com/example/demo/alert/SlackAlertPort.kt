package com.example.demo.alert

import com.example.demo.constant.Constant
import com.example.demo.member.Member
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class SlackAlertPort : AlertPort {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun send(member: Member) {
        val msg = """${member.id},${member.name},${member.level}"""
        send(msg)
    }

    override fun send(msg: String) {
        val restTemplate =
            RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val bodyMap = hashMapOf<String, String>()
        bodyMap["text"] = msg

        val httpEntity = HttpEntity(bodyMap, headers)

        val url = Constant.SLACK_URL
        try {
            val response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String::class.java)
            if (response.statusCode.value() != 200) {
                log.warn("슬랙통신 상태코드이상 상태코드={}", response.statusCode.value())
                throw RuntimeException("슬랙 response http상태코드가 200이 아님")
            }
            log.info("성공 메시지={}", msg)
        } catch (e: Exception) {
            log.warn("슬랙통신에 문제발생 스택트레이스={}", e.stackTrace)
        }
    }
}
