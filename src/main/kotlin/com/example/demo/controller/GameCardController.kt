package com.example.demo.controller

import com.example.demo.common.Result
import com.example.demo.domain.gamecard.dto.GameCardDeleteDto
import com.example.demo.domain.gamecard.dto.GameCardInsertDto
import com.example.demo.domain.gamecard.dto.GameCardResponseDto
import com.example.demo.domain.gamecard.service.GameCardService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gamecard")
class GameCardController(val gameCardService: GameCardService) {
    @GetMapping("/all")
    fun findByMember(
        @RequestParam("memberId") memberId: Long,
    ): Result<List<GameCardResponseDto>> {
        val gameCardList = gameCardService.findByMember(memberId)
        val resDtoList = gameCardList.map { GameCardResponseDto.domainToDto(it) }
        return Result(resDtoList)
    }

    @PostMapping("/save")
    fun save(
        @Valid @RequestBody gameCardInsertDto: GameCardInsertDto,
    ) {
        gameCardService.save(gameCardInsertDto)
    }

    @DeleteMapping("{id}")
    fun delete(
        @PathVariable id: Long,
    ) {
        val gameCardDeleteDto = GameCardDeleteDto(id)
        gameCardService.delete(gameCardDeleteDto)
    }
}
