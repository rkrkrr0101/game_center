package com.example.demo.domain.game.repository

import com.example.demo.domain.game.Game
import org.springframework.data.jpa.repository.JpaRepository

interface GameJpaRepository : JpaRepository<Game, Long>
