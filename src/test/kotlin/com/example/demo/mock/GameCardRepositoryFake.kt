package com.example.demo.mock

import com.example.demo.gamecard.GameCard
import com.example.demo.gamecard.repository.GameCardRepository
import com.example.demo.member.Member
import jakarta.persistence.EntityNotFoundException
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import java.util.*
import java.util.concurrent.atomic.AtomicLong


class GameCardRepositoryFake:GameCardRepository {
    private var generatedId: AtomicLong = AtomicLong(0)
    private val gameCardList:MutableList<GameCard> = Collections.synchronizedList(mutableListOf<GameCard>())
    override fun save(gameCard: GameCard) {
        if(gameCard.id==null || gameCard.id==0L){
            val newGameCard = GameCard(
                gameCard.title,
                gameCard.serialNo,
                gameCard.price,
                gameCard.game,
                gameCard.member,
                generatedId.incrementAndGet()
            )
            gameCardList.add(newGameCard)
        }else{
            gameCardList.removeIf { it.id==gameCard.id }
            gameCardList.add(gameCard)
        }
    }

    override fun delete(gameCard: GameCard) {
        val removeIf = gameCardList.removeIf { it.id == gameCard.id }
        if (removeIf==false){
            throw JpaObjectRetrievalFailureException(EntityNotFoundException("delete예외발생"))
        }
    }

    override fun findByMember(member: Member): List<GameCard> {
        return gameCardList.filter { it.member.id==member.id }
    }

    override fun findByGameAndSerialNo(title: String, serialNo: Long): GameCard? {
        return gameCardList.find { it.game.title==title && it.serialNo==serialNo }
    }

    override fun deleteByMember(member: Member) {
        gameCardList.removeIf { it.member.id==member.id }
    }

    override fun findById(id: Long): GameCard {
        return gameCardList.find { it.id==id }
            ?:throw JpaObjectRetrievalFailureException(EntityNotFoundException("findById예외발생"))
    }
}