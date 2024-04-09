package com.example.demo.game.controller

import com.example.demo.common.Result
import com.example.demo.game.dto.GameResponseDto
import com.example.demo.game.service.GameService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
class GameControllerImpl(val gameService: GameService) {
    @GetMapping("/all")
    fun findAll(): Result<List<GameResponseDto>> {
        val gameList = gameService.findAll()
        val dtoList = gameList.map { GameResponseDto.domainToDto(it) }
        return Result(dtoList)
    }
}
