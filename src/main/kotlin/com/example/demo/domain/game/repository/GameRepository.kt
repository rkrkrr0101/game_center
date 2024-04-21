package com.example.demo.domain.game.repository

import com.example.demo.domain.game.Game

interface GameRepository {
    fun findAll(): List<Game>

    fun findById(id: Long): Game
}
