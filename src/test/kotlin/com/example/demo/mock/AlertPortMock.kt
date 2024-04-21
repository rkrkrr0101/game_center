package com.example.demo.mock

import com.example.demo.alert.AlertPort
import com.example.demo.domain.member.Member

class AlertPortMock : AlertPort {
    override fun send(msg: String) {
    }

    override fun send(member: Member) {
    }
}
