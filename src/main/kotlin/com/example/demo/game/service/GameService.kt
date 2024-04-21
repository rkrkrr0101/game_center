package com.example.demo.game.service

import com.example.demo.game.Game
import com.example.demo.game.repository.GameRepository
import org.springframework.stereotype.Service

@Service
class GameService(val gameRepository: GameRepository) {
    fun findAll(): List<Game> {
        return gameRepository.findAll()
    }
}
