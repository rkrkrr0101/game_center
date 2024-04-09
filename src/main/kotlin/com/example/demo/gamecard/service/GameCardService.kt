package com.example.demo.gamecard.service

import com.example.demo.gamecard.GameCard
import com.example.demo.gamecard.dto.GameCardDeleteDto
import com.example.demo.gamecard.dto.GameCardInsertDto


interface GameCardService {
    fun findByMember(memberId:Long):List<GameCard>
    fun save(gameCardInsertDto: GameCardInsertDto)
    fun delete(gameCardDeleteDto: GameCardDeleteDto)
}