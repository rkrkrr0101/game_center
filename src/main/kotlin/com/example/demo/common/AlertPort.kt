package com.example.demo.common

import com.example.demo.domain.member.Member

interface AlertPort {
    fun send(member: Member)
}
