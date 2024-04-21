package com.example.demo.domain.member.dto

import com.example.demo.constant.Level
import com.example.demo.domain.member.Member
import java.time.LocalDate

data class MemberResponseDto(
    val id: Long,
    val name: String,
    val email: String,
    val joinDate: LocalDate,
    val level: Level,
    val totalCardQuantity: Int,
    val totalCardPrice: String,
) {
    companion object {
        fun domainToDto(member: Member): MemberResponseDto {
            return MemberResponseDto(
                member.id,
                member.name,
                member.email,
                member.joinDate,
                member.level,
                member.totalCardQuantity,
                member.totalCardPrice.toPlainString(),
            )
        }
    }
}
