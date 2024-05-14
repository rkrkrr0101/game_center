package com.example.demo.api.resultType

import org.springframework.http.HttpStatus

data class ErrorResult<T>(val httpStatus: HttpStatus, val message: T)
