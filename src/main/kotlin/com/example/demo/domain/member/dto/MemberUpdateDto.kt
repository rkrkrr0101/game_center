package com.example.demo.domain.member.dto

import com.example.demo.customdate.CustomDate
import com.example.demo.exception.exception.DatePastException
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class MemberUpdateDto(
    @field:Size(min = 2, max = 100, message = "회원의 이름길이를 확인해주세요")
    @field:NotBlank(message = "회원의 이름이 공백이면 안됩니다")
    val name: String,
    @field:Email(message = "회원의 이메일을 다시 확인해 주세요")
    @field:NotBlank(message = "회원의 이메일이 공백이면 안됩니다")
    val email: String,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    @field:PastOrPresent
    val joinDate: LocalDate,
    var id: Long = 0,
) {
    lateinit var customDate: CustomDate

    fun checkJoinDateYear() {
        val pestDate = customDate.now().minusYears(1)
        if (joinDate < pestDate) {
            throw DatePastException("회원의 가입날짜가 1년 이전이면 안됩니다", joinDate)
        }
    }
}
