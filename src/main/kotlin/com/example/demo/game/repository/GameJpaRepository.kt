package com.example.demo.game.repository

import com.example.demo.game.Game
import org.springframework.data.jpa.repository.JpaRepository

interface GameJpaRepository : JpaRepository<Game, Long>
