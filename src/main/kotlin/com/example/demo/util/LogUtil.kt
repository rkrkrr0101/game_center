package com.example.demo.util

import com.example.demo.exception.exception.EmailDuplicateException
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

        fun emailDuplicateThrow(
            action: String,
            email: String,
        ): Nothing {
            log.warn("멤버의 {}중 이메일중복:{}", action, email)
            throw EmailDuplicateException("이메일이 중복입니다", email)
        }
    }
}
