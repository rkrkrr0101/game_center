package com.example.demo.mock

import com.example.demo.game.Game
import com.example.demo.game.repository.GameRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import java.util.*

class GameRepositoryFake:GameRepository {

    private val gameList:MutableList<Game> = Collections.synchronizedList(mutableListOf<Game>())
    init {
        val game1 = Game("pokemon", 1)
        val game2 = Game("mtg", 2)
        val game3 = Game("yugioh", 3)

        gameList.add(game1)
        gameList.add(game2)
        gameList.add(game3)
    }
    override fun findAll(): List<Game> {
        return gameList
    }

    override fun findById(id: Long): Game {
        return gameList.find { it.id==id }
            ?:throw JpaObjectRetrievalFailureException(EntityNotFoundException("findById예외발생"))
    }
}