package com.example.demo.domain.gamecard.repository

import com.example.demo.domain.game.Game
import com.example.demo.domain.gamecard.GameCard
import com.example.demo.domain.gamecard.service.GameCardRepository
import com.example.demo.domain.member.Member
import com.example.demo.util.LogUtil
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrElse

@Repository
class GameCardRepositoryImpl(val gameCardJpaRepository: GameCardJpaRepository) : GameCardRepository {
    override fun findByMember(member: Member): List<GameCard> {
        return gameCardJpaRepository.findByMember(member)
    }

    override fun findByGameAndSerialNo(
        game: Game,
        serialNo: Long,
    ): GameCard? {
        return gameCardJpaRepository.findByGameAndSerialNo(game, serialNo)
    }

    override fun deleteByMember(member: Member) {
        gameCardJpaRepository.deleteByMember(member)
    }

    override fun findById(id: Long): GameCard {
        return gameCardJpaRepository.findById(id).getOrElse {
            LogUtil.emptyFindThrow("gameCard", id)
        }
    }
}
