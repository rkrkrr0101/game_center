package com.example.demo.domain.game.service

import com.example.demo.domain.game.Game
import org.springframework.stereotype.Service

@Service
class GameService(val gameRepository: GameRepository) {
    fun findAll(): List<Game> {
        return gameRepository.findAll()
    }
}
