package com.example.demo.unittest.game.controller

import com.example.demo.game.Game
import com.example.demo.game.controller.GameControllerImpl
import com.example.demo.game.dto.GameResponseDto
import com.example.demo.game.service.GameService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class GameControllerTest {
    private val gameService = mockk<GameService>()
    private val gameController = GameControllerImpl(gameService)

    @Test
    fun findAll의_반환을_GameResponseDto로_반환한다()  {
        // g
        val gameList = mutableListOf<Game>()
        gameList.add(Game("poke", 1))
        gameList.add(Game("yugi", 2))
        gameList.add(Game("mtg", 3))
        every { gameService.findAll() } returns gameList
        // w
        val findGame = gameController.findAll().data[0]
        // t
        Assertions.assertThat(findGame).isInstanceOf(GameResponseDto::class.java)
    }
}
