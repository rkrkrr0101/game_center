package com.example.demo.domain.gamecard.repository

import com.example.demo.domain.gamecard.GameCard
import com.example.demo.domain.member.Member
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrElse

@Repository
class GameCardRepositoryImpl(val gameCardJpaRepository: GameCardJpaRepository) : GameCardRepository {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun findByMember(member: Member): List<GameCard> {
        return gameCardJpaRepository.findByMember(member)
    }

    override fun findByGameAndSerialNo(
        title: String,
        serialNo: Long,
    ): GameCard? {
        return gameCardJpaRepository.findByGameAndSerialNo(title, serialNo)
    }

    override fun deleteByMember(member: Member) {
        gameCardJpaRepository.deleteByMember(member)
    }

    override fun findById(id: Long): GameCard {
        return gameCardJpaRepository.findById(id).getOrElse {
            log.warn("id={}의 게임카드를 찾을수 없습니다", id)
            throw EntityNotFoundException("""gameCard의 객체를 찾을수없음 id=$id""")
        }
    }
}
