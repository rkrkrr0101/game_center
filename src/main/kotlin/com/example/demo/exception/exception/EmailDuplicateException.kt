package com.example.demo.exception.exception

class EmailDuplicateException(val msg: String, val email: String) : RuntimeException(msg)
