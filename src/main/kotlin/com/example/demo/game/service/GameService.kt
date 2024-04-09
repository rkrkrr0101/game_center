package com.example.demo.game.service

import com.example.demo.game.Game

interface GameService {
    fun findAll():List<Game>
}