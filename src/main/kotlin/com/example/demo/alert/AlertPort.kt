package com.example.demo.alert

import com.example.demo.domain.member.Member

interface AlertPort {
    fun send(msg: String)

    fun send(member: Member)
}
