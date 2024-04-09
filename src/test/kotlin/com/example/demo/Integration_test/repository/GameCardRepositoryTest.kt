package com.example.demo.Integration_test.repository

import com.example.demo.game.repository.GameRepository
import com.example.demo.gamecard.GameCard
import com.example.demo.gamecard.repository.GameCardRepository
import com.example.demo.member.Member
import com.example.demo.member.repository.MemberRepository
import com.example.demo.mock.TestConstant
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@Transactional
class GameCardRepositoryTest @Autowired constructor(
    val gameCardRepository: GameCardRepository,
    val memberRepository: MemberRepository,
    val gameRepository: GameRepository){

    @Test
    fun gameCard를_저장할수_있다(){
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)
        val gameList = gameRepository.findAll()
        val findGame = gameList.find { it.title == "yugioh" }?:throw Exception()
        val gameCard = GameCard("tt", 123, BigDecimal(30), findGame, member)

        gameCardRepository.save(gameCard)
        val findGameCard = gameCardRepository.findById(gameCard.id)

        Assertions.assertThat(findGameCard.id).isEqualTo(gameCard.id)
    }
    @Test
    fun gameCard를_삭제할수_있다(){
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)
        val gameList = gameRepository.findAll()
        val findGame = gameList.find { it.title == "yugioh" }?:throw Exception()
        val gameCard = GameCard("tt", 123, BigDecimal(30), findGame, member)
        gameCardRepository.save(gameCard)

        gameCardRepository.delete(gameCard)
        val gameCardList = gameCardRepository.findByMember(member)

        Assertions.assertThat(gameCardList.size).isEqualTo(0)
    }
    @Test
    fun gameCard를_멤버로_검색할수_있다(){
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)
        val gameList = gameRepository.findAll()
        val findGame = gameList.find { it.title == "yugioh" }?:throw Exception()
        val gameCard1 = GameCard("tt", 123, BigDecimal(30), findGame, member)
        gameCardRepository.save(gameCard1)
        val gameCard2 = GameCard("ttt", 1234, BigDecimal(31), findGame, member)
        gameCardRepository.save(gameCard2)
        val gameCard3 = GameCard("ttt", 1235, BigDecimal(32), findGame, member)
        gameCardRepository.save(gameCard3)

        val gameCardList = gameCardRepository.findByMember(member)

        Assertions.assertThat(gameCardList.size).isEqualTo(3)
    }
    @Test
    fun gameCard를_일련번호와_게임으로_검색할수_있다(){
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)
        val gameList = gameRepository.findAll()
        val yugiohGame = gameList.find { it.title == "yugioh" }?:throw Exception()
        val pokemonGame = gameList.find { it.title == "pokemon" }?:throw Exception()
        val mtgGame = gameList.find { it.title == "mtg" }?:throw Exception()
        val gameCard1 = GameCard("tt", 123, BigDecimal(30), yugiohGame, member)
        gameCardRepository.save(gameCard1)
        val gameCard2 = GameCard("ttt", 1234, BigDecimal(31), pokemonGame, member)
        gameCardRepository.save(gameCard2)
        val gameCard3 = GameCard("ttt", 1235, BigDecimal(32), mtgGame, member)
        gameCardRepository.save(gameCard3)

        val findGameCard = gameCardRepository.findByGameAndSerialNo(gameCard1.game.title, gameCard1.serialNo)
            ?:throw Exception()

        Assertions.assertThat(findGameCard.game.title).isEqualTo(gameCard1.game.title)
        Assertions.assertThat(findGameCard.serialNo).isEqualTo(gameCard1.serialNo)
    }

    @Test
    fun 멤버가_소유하고있는_모든_카드를_삭제할수_있다(){
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)
        val gameList = gameRepository.findAll()
        val yugiohGame = gameList.find { it.title == "yugioh" }?:throw Exception()
        val pokemonGame = gameList.find { it.title == "pokemon" }?:throw Exception()
        val mtgGame = gameList.find { it.title == "mtg" }?:throw Exception()
        val gameCard1 = GameCard("tt", 123, BigDecimal(30), yugiohGame, member)
        gameCardRepository.save(gameCard1)
        val gameCard2 = GameCard("ttt", 1234, BigDecimal(31), pokemonGame, member)
        gameCardRepository.save(gameCard2)
        val gameCard3 = GameCard("ttt", 1235, BigDecimal(32), mtgGame, member)
        gameCardRepository.save(gameCard3)

        gameCardRepository.deleteByMember(member)
        val gameCardList = gameCardRepository.findByMember(member)

        Assertions.assertThat(gameCardList.size).isEqualTo(0)

    }
    @Test
    fun gameCard의_id로_조회할수_있다(){
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)
        val gameList = gameRepository.findAll()
        val yugiohGame = gameList.find { it.title == "yugioh" }?:throw Exception()
        val gameCard1 = GameCard("tt", 123, BigDecimal(30), yugiohGame, member)
        gameCardRepository.save(gameCard1)

        val findGameCard = gameCardRepository.findById(gameCard1.id)

        Assertions.assertThat(findGameCard.id).isEqualTo(gameCard1.id)
    }
    @Test
    fun gameCard의_id로_조회할때_해당_id가_없으면_예외가_발생한다(){
        Assertions.assertThatThrownBy { gameCardRepository.findById(100)  }
            .isInstanceOf(JpaObjectRetrievalFailureException::class.java)
    }

}