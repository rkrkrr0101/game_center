package com.example.demo.util

import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory

class LogUtil {
    companion object {
        private val log = LoggerFactory.getLogger(Companion::class.java)

        fun emptyFindThrow(
            typeName: String,
            id: Long,
        ): Nothing {
            log.warn("id={}의 {}를 찾을수 없습니다", id, typeName)
            throw EntityNotFoundException("""$typeName 의 객체를 찾을수없음 id=$id""")
        }
    }
}
