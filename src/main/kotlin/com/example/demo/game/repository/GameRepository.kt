package com.example.demo.game.repository

import com.example.demo.game.Game

interface GameRepository {
    fun findAll(): List<Game>

    fun findById(id: Long): Game
}
