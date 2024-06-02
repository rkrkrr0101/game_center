package com.example.demo.util

import com.example.demo.domain.game.Game
import com.example.demo.domain.gamecard.GameCard
import com.example.demo.domain.member.Member
import java.math.BigDecimal

class TestFactory {
    companion object {
        fun createGameCard(
            title: String,
            serialNo: Long,
            price: BigDecimal,
            game: Game,
            member: Member,
            id: Long = 0L,
        ): GameCard {
            return GameCard(title, serialNo, price, game, member, id)
        }
    }
}
