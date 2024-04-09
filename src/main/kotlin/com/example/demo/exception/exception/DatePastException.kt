package com.example.demo.exception.exception

import java.time.LocalDate

class DatePastException(val msg: String, val date: LocalDate) : RuntimeException(msg)
