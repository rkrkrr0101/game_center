package com.example.demo.domain.gamecard.service

import com.example.demo.domain.game.Game
import com.example.demo.domain.gamecard.GameCard
import com.example.demo.domain.member.Member

interface GameCardRepository {
    fun findByMember(member: Member): List<GameCard>

    fun findByGameAndSerialNo(
        game: Game,
        serialNo: Long,
    ): GameCard?

    fun deleteByMember(member: Member)

    fun findById(id: Long): GameCard
}
