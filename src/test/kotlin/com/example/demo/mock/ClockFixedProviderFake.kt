package com.example.demo.mock

import jakarta.validation.ClockProvider
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class ClockFixedProviderFake(private val fixedTime:String):ClockProvider {
    override fun getClock(): Clock {
        return Clock.fixed(Instant.parse(fixedTime),ZoneId.of("Asia/Seoul"))
    }
}