package com.example.demo.gamecard.repository

import com.example.demo.gamecard.GameCard
import com.example.demo.member.Member

interface GameCardRepository {
    fun save(gameCard: GameCard)

    fun delete(gameCard: GameCard)

    fun findByMember(member: Member): List<GameCard>

    fun findByGameAndSerialNo(
        title: String,
        serialNo: Long,
    ): GameCard?

    fun deleteByMember(member: Member)

    fun findById(id: Long): GameCard
}
