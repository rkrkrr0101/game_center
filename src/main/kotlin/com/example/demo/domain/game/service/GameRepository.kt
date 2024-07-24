package com.example.demo.domain.game.service

import com.example.demo.domain.game.Game

interface GameRepository {
    fun findAll(): List<Game>

    fun findById(id: Long): Game
}
