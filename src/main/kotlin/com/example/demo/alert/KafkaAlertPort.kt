package com.example.demo.alert

import com.example.demo.domain.member.Member
import org.slf4j.LoggerFactory

class KafkaAlertPort : AlertPort {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun send(msg: String) {
        TODO("Not yet implemented")
    }

    override fun send(member: Member) {
        TODO("Not yet implemented")
    }
}
