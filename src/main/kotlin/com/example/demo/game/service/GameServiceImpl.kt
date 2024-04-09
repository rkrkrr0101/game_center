package com.example.demo.game.service

import com.example.demo.game.Game
import com.example.demo.game.repository.GameRepository
import org.springframework.stereotype.Service

@Service
class GameServiceImpl(val gameRepository: GameRepository):GameService {
    override fun findAll(): List<Game> {
        return gameRepository.findAll()
    }
}