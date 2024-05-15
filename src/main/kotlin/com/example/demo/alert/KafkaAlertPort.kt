package com.example.demo.alert

import com.example.demo.constant.Constant
import com.example.demo.domain.member.Member
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaAlertPort(val template: KafkaTemplate<String, String>) : AlertPort {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun send(member: Member) {
        val msg = """${member.id},${member.name},${member.level}"""
        send(member.id, msg)
    }

    private fun send(
        key: Long,
        msg: String,
    ) {
        val send = template.send(Constant.TOPIC_NAME, key.toString(), msg)
        send.whenComplete { _, ex ->
            if (ex != null) {
                log.warn("카프카 전송실패 key=$key 메시지=$msg 스택트레이스=${ex.stackTrace}")
            }
        }
    }
}
