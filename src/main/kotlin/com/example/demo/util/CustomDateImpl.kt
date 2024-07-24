package com.example.demo.util

import com.example.demo.common.CustomDate
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class CustomDateImpl : CustomDate {
    override fun now(): LocalDate {
        return LocalDate.now()
    }
}
