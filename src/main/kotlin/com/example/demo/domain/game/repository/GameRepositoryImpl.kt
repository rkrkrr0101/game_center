package com.example.demo.domain.game.repository

import com.example.demo.domain.game.Game
import com.example.demo.domain.game.service.GameRepository
import com.example.demo.util.LogUtil
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrElse

@Repository
class GameRepositoryImpl(val gameJpaRepository: GameJpaRepository) : GameRepository {
    override fun findById(id: Long): Game {
        return gameJpaRepository.findById(id).getOrElse {
            LogUtil.emptyFindThrow("game", id)
        }
    }

    override fun findAll(): List<Game> {
        return gameJpaRepository.findAll()
    }
}
