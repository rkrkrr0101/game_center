package com.example.demo.customdate

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class CustomDateImpl : CustomDate {
    override fun now(): LocalDate {
        return LocalDate.now()
    }
}
