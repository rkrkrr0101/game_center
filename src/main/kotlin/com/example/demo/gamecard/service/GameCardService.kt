package com.example.demo.gamecard.service

import com.example.demo.alert.AlertPort
import com.example.demo.exception.exception.GameCardDuplicateException
import com.example.demo.game.repository.GameRepository
import com.example.demo.gamecard.GameCard
import com.example.demo.gamecard.dto.GameCardDeleteDto
import com.example.demo.gamecard.dto.GameCardInsertDto
import com.example.demo.gamecard.repository.GameCardRepository
import com.example.demo.member.Member
import com.example.demo.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GameCardService(
    val gameCardRepository: GameCardRepository,
    val memberRepository: MemberRepository,
    val gameRepository: GameRepository,
    val alertPort: AlertPort,
) {
    @Transactional(readOnly = true)
    fun findByMember(memberId: Long): List<GameCard> {
        val findMember = memberRepository.findById(memberId)
        return gameCardRepository.findByMember(findMember)
    }

    @Transactional
    fun save(gameCardInsertDto: GameCardInsertDto) {
        val findGame = gameRepository.findById(gameCardInsertDto.gameId)
        if (!isUniqueSerialNo(findGame.title, gameCardInsertDto.serialNo)) {
            throw GameCardDuplicateException(
                "카드의 일련번호가 중복입니다",
                findGame.title,
                gameCardInsertDto.serialNo,
            )
        }
        val member = memberRepository.findById(gameCardInsertDto.memberId)

        val gameCard =
            GameCard(
                gameCardInsertDto.title,
                gameCardInsertDto.serialNo,
                gameCardInsertDto.price,
                findGame,
                member,
            )
        val prevLevel = member.level
        member.addGameCard(gameCard)
        val curLevel = member.level
        if (curLevel != prevLevel) {
            levelAlertCall(member)
        }
    }

    @Transactional
    fun delete(gameCardDeleteDto: GameCardDeleteDto) {
        val gameCard = gameCardRepository.findById(gameCardDeleteDto.id)
        val member = memberRepository.findById(gameCard.member.id)

        val prevLevel = member.level
        member.removeGameCard(gameCard)
        val curLevel = member.level

        if (curLevel != prevLevel) {
            levelAlertCall(member)
        }
    }

    private fun isUniqueSerialNo(
        title: String,
        serialNo: Long,
    ): Boolean {
        gameCardRepository.findByGameAndSerialNo(title, serialNo) ?: return true
        return false
    }

    private fun levelAlertCall(member: Member) {
        alertPort.send(member)
    }
}
