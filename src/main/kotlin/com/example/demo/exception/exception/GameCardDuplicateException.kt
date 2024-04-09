package com.example.demo.exception.exception

class GameCardDuplicateException(val msg: String, val gameTitle: String, val serialNo: Long) : RuntimeException(msg)
