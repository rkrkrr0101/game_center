package com.example.demo.mock

import com.example.demo.common.CustomDate
import java.time.LocalDate

class CustomDateFake(private val fixedDate: String) : CustomDate {
    override fun now(): LocalDate {
        return LocalDate.parse(fixedDate)
    }
}
