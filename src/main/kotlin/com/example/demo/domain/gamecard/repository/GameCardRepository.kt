package com.example.demo.domain.gamecard.repository

import com.example.demo.domain.gamecard.GameCard
import com.example.demo.domain.member.Member

interface GameCardRepository {
    fun findByMember(member: Member): List<GameCard>

    fun findByGameAndSerialNo(
        title: String,
        serialNo: Long,
    ): GameCard?

    fun deleteByMember(member: Member)

    fun findById(id: Long): GameCard
}
