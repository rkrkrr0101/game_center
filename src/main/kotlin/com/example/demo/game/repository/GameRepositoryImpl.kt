package com.example.demo.game.repository

import com.example.demo.game.Game
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrElse

@Repository
class GameRepositoryImpl(val gameJpaRepository: GameJpaRepository) : GameRepository {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun findById(id: Long): Game {
        return gameJpaRepository.findById(id).getOrElse {
            log.warn("id={}의 게임의 찾을수 없습니다", id)
            throw EntityNotFoundException("""game의 객체를 찾을수없음 id=$id""")
        }
    }

    override fun findAll(): List<Game> {
        return gameJpaRepository.findAll()
    }
}
