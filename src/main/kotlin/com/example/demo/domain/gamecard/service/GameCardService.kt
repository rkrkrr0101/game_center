package com.example.demo.domain.gamecard.service

import com.example.demo.alert.AlertPort
import com.example.demo.constant.Level
import com.example.demo.domain.game.Game
import com.example.demo.domain.game.repository.GameRepository
import com.example.demo.domain.gamecard.GameCard
import com.example.demo.domain.gamecard.dto.GameCardDeleteDto
import com.example.demo.domain.gamecard.dto.GameCardInsertDto
import com.example.demo.domain.gamecard.repository.GameCardRepository
import com.example.demo.domain.member.Member
import com.example.demo.domain.member.repository.MemberRepository
import com.example.demo.exception.exception.GameCardDuplicateException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GameCardService(
    val gameCardRepository: GameCardRepository,
    val memberRepository: MemberRepository,
    val gameRepository: GameRepository,
    val alertPortList: List<AlertPort>,
) {
    @Transactional(readOnly = true)
    fun findByMember(memberId: Long): List<GameCard> {
        val findMember = memberRepository.findById(memberId)
        return gameCardRepository.findByMember(findMember)
    }

    @Transactional
    fun save(gameCardInsertDto: GameCardInsertDto) {
        val game = gameRepository.findById(gameCardInsertDto.gameId)

        requireUniqueGameCard(game, gameCardInsertDto.serialNo)

        val member = memberRepository.findById(gameCardInsertDto.memberId)
        val gameCard =
            GameCard(
                gameCardInsertDto.title,
                gameCardInsertDto.serialNo,
                gameCardInsertDto.price,
                game,
                member,
            )
        val prevLevel = member.level
        member.addGameCard(gameCard)

        checkAlert(prevLevel, member)
    }

    @Transactional
    fun delete(gameCardDeleteDto: GameCardDeleteDto) {
        val gameCard = gameCardRepository.findById(gameCardDeleteDto.id)
        val member = memberRepository.findById(gameCard.member.id)

        val prevLevel = member.level
        member.removeGameCard(gameCard)

        checkAlert(prevLevel, member)
    }

    private fun checkAlert(
        prevLevel: Level,
        member: Member,
    ) {
        val curLevel = member.level
        if (curLevel != prevLevel) {
            levelAlertCall(member)
        }
    }

    private fun requireUniqueGameCard(
        game: Game,
        serialNo: Long,
    ) {
        if (!isUniqueSerialNo(game, serialNo)) {
            throw GameCardDuplicateException(
                "카드의 일련번호가 중복입니다",
                game.title,
                serialNo,
            )
        }
    }

    private fun isUniqueSerialNo(
        game: Game,
        serialNo: Long,
    ): Boolean {
        gameCardRepository.findByGameAndSerialNo(game, serialNo) ?: return true
        return false
    }

    private fun levelAlertCall(member: Member) {
        for (alertPort in alertPortList) {
            alertPort.send(member)
        }
    }
}
