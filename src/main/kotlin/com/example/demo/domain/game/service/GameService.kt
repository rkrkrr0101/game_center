package com.example.demo.domain.game.service

import com.example.demo.domain.game.Game
import com.example.demo.domain.game.repository.GameRepository
import org.springframework.stereotype.Service

@Service
class GameService(val gameRepository: GameRepository) {
    fun findAll(): List<Game> {
        return gameRepository.findAll()
    }
}
