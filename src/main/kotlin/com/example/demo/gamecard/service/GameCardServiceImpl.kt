package com.example.demo.gamecard.service

import com.example.demo.alert.AlertPort
import com.example.demo.exception.exception.GameCardDuplicateException
import com.example.demo.game.repository.GameRepository
import com.example.demo.gamecard.GameCard
import com.example.demo.gamecard.dto.GameCardDeleteDto
import com.example.demo.gamecard.dto.GameCardInsertDto
import com.example.demo.gamecard.repository.GameCardRepository
import com.example.demo.member.Member
import com.example.demo.caltotalcardmanager.CalTotalCardManager
import com.example.demo.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class GameCardServiceImpl(val gameCardRepository: GameCardRepository,
                          val memberRepository: MemberRepository,
                          val gameRepository: GameRepository,
                          val alertPort: AlertPort):GameCardService {
    @Transactional(readOnly = true)
    override fun findByMember(memberId: Long): List<GameCard> {
        val findMember = memberRepository.findById(memberId)
        return gameCardRepository.findByMember(findMember)
    }
    @Transactional
    override fun save(gameCardInsertDto: GameCardInsertDto) {
        val findGame = gameRepository.findById(gameCardInsertDto.gameId)
        if (!isUniqueSerialNo(findGame.title,gameCardInsertDto.serialNo)){
            throw GameCardDuplicateException(
                "카드의 일련번호가 중복입니다",
                findGame.title,
                gameCardInsertDto.serialNo
            )
        }
        val findMember = memberRepository.findById(gameCardInsertDto.memberId)

        val gameCard = GameCard(
            gameCardInsertDto.title,
            gameCardInsertDto.serialNo,
            gameCardInsertDto.price,
            findGame,
            findMember
        )
        gameCardRepository.save(gameCard)
        calMemberLevel(findMember)
    }

    @Transactional
    override fun delete(gameCardDeleteDto: GameCardDeleteDto) {
        val findGameCard = gameCardRepository.findById(gameCardDeleteDto.id)
        val findMember = memberRepository.findById(findGameCard.member.id)
        gameCardRepository.delete(findGameCard)
        calMemberLevel(findMember)

    }
    private fun isUniqueSerialNo(title:String,serialNo:Long):Boolean{
        gameCardRepository.findByGameAndSerialNo(title, serialNo)?:return true
        return false
    }
    private fun calMemberLevel(member: Member) {
        val prevLevel = member.level
        val findGameCardList = gameCardRepository.findByMember(member)
        val calTotalCardManager = CalTotalCardManager()

        val (calQuantity, calPrice) = calTotalCardManager.calTotalCardQuantityAndPrice(findGameCardList)
        val calLevel = calTotalCardManager.calLevel(findGameCardList)

        member.totalCardQuantity=calQuantity
        member.totalCardPrice=calPrice
        member.level=calLevel

        val curLevel = member.level
        if (curLevel!=prevLevel){
            levelAlertCall(member)
        }
    }
    private fun levelAlertCall(member: Member){
        alertPort.send(member)
    }

}