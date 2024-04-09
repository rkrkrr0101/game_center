package com.example.demo.common

import org.springframework.http.HttpStatus

data class ErrorResult<T>(val httpStatus: HttpStatus, val message: T)
