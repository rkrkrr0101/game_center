package com.example.demo.mock

import com.example.demo.alert.AlertPort
import com.example.demo.domain.member.Member

class SlackPortMock : AlertPort {
    override fun send(member: Member) {
    }
}
