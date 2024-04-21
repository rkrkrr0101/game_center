package com.example.demo.domain.gamecard.dto

import com.example.demo.domain.gamecard.GameCard
import java.math.BigDecimal

data class GameCardResponseDto(
    val title: String,
    val serialNo: Long,
    val price: BigDecimal,
    val gameTitle: String,
    val memberId: Long,
    val id: Long,
) {
    companion object {
        fun domainToDto(gameCard: GameCard): GameCardResponseDto {
            return GameCardResponseDto(
                gameCard.title,
                gameCard.serialNo,
                gameCard.price,
                gameCard.game.title,
                gameCard.member.id,
                gameCard.id,
            )
        }
    }
}
