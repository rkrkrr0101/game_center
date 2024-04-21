package com.example.demo.domain.game.dto

import com.example.demo.domain.game.Game

data class GameResponseDto(val title: String, val id: Long) {
    companion object {
        fun domainToDto(game: Game): GameResponseDto {
            return GameResponseDto(game.title, game.id)
        }
    }
}
