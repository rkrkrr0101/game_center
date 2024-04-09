package com.example.demo.unittest.gamecard.service

import com.example.demo.constant.Level
import com.example.demo.exception.exception.GameCardDuplicateException
import com.example.demo.gamecard.GameCard
import com.example.demo.gamecard.dto.GameCardDeleteDto
import com.example.demo.gamecard.dto.GameCardInsertDto
import com.example.demo.gamecard.service.GameCardServiceImpl
import com.example.demo.member.Member
import com.example.demo.mock.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Sort
import java.math.BigDecimal
import java.time.LocalDate

class GameCardServiceTest {
    val gameCardRepository = GameCardRepositoryFake()
    val memberRepository = MemberRepositoryFake()
    val gameRepository = GameRepositoryFake()
    val alertPort = AlertPortMock()
    val gameCardService = GameCardServiceImpl(gameCardRepository, memberRepository, gameRepository, alertPort)

    @Test
    fun gameCard를_멤버단위로_조회할수있다()  {
        // g
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]
        val gameCard1 = GameCard("aaa", 1, BigDecimal(30), game, findMember)
        val gameCard2 = GameCard("bbb", 2, BigDecimal(40), game, findMember)
        gameCardRepository.save(gameCard1)
        gameCardRepository.save(gameCard2)
        // w
        val findGameCardList = gameCardService.findByMember(findMember.id)
        // t
        Assertions.assertThat(findGameCardList.size).isEqualTo(2)
    }

    @Test
    fun gameCard를_저장할수있다()  {
        // g
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]
        val gameCard1 = GameCardInsertDto("aaa", 1, BigDecimal(30), game.id, findMember.id)
        // w
        gameCardService.save(gameCard1)
        val findGameCardList = gameCardRepository.findByMember(findMember)
        // t
        Assertions.assertThat(findGameCardList.size).isEqualTo(1)
    }

    @Test
    fun gameCard를_저장할때_멤버의_총가격이_바뀐다()  {
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]

        // w
        val gameCardDto = GameCardInsertDto("aaa", 1, BigDecimal(30), game.id, findMember.id)
        gameCardService.save(gameCardDto)
        val assertFindMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        // t
        Assertions.assertThat(assertFindMember.totalCardPrice)
            .isLessThan(BigDecimal(30.1))
            .isGreaterThan(BigDecimal(29.9))
    }

    @Test
    fun gameCard를_저장할때_멤버의_총카드수가_바뀐다()  {
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]

        // w
        val gameCardDto = GameCardInsertDto("aaa", 1, BigDecimal(30), game.id, findMember.id)
        gameCardService.save(gameCardDto)
        val assertFindMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        // t
        Assertions.assertThat(assertFindMember.totalCardQuantity).isEqualTo(1)
    }

    @Test
    fun gameCard를_저장할때_멤버의_레벨이_바뀐다()  {
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]

        // w
        val gameCardDto = GameCardInsertDto("aaa", 1, BigDecimal(30), game.id, findMember.id)
        gameCardService.save(gameCardDto)
        val assertFindMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        // t
        Assertions.assertThat(assertFindMember.level).isEqualTo(Level.Silver)
    }

    @Test
    fun gameCard를_저장할때_게임명과_일련번호가_중복이면_예외가_발생한다()  {
        // g
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]
        val gameCardDto1 = GameCardInsertDto("aaa", 1, BigDecimal(30), game.id, findMember.id)
        val gameCardDto2 = GameCardInsertDto("bbb", 1, BigDecimal(40), game.id, findMember.id)
        gameCardService.save(gameCardDto1)
        // w,t
        Assertions.assertThatThrownBy { gameCardService.save(gameCardDto2) }
            .isInstanceOf(GameCardDuplicateException::class.java)
    }

    @Test
    fun gameCard를_삭제할수_있다()  {
        // g
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]
        val gameCard1 = GameCard("aaa", 1, BigDecimal(30), game, findMember)
        val gameCard2 = GameCard("bbb", 2, BigDecimal(40), game, findMember)
        gameCardRepository.save(gameCard1)
        gameCardRepository.save(gameCard2)
        val deleteDto = GameCardDeleteDto(findMember.id)
        // w
        gameCardService.delete(deleteDto)
        val gameCardList = gameCardRepository.findByMember(findMember)

        // t
        Assertions.assertThat(gameCardList.size).isEqualTo(1)
    }

    @Test
    fun gameCard를_삭제할때_멤버의_총가격이_바뀐다()  {
        // g
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]
        val gameCard1 = GameCard("aaa", 1, BigDecimal(30), game, findMember, 1)
        val gameCard2 = GameCard("bbb", 2, BigDecimal(40), game, findMember, 2)
        gameCardRepository.save(gameCard1)
        gameCardRepository.save(gameCard2)
        val deleteDto = GameCardDeleteDto(gameCard2.id)
        // w
        gameCardService.delete(deleteDto)
        val assertMember = memberRepository.findById(findMember.id)

        // t
        Assertions.assertThat(assertMember.totalCardPrice)
            .isLessThan(BigDecimal(30.1))
            .isGreaterThan(BigDecimal(29.9))
    }

    @Test
    fun gameCard를_삭제할때_멤버의_총카드수가_바뀐다()  {
        // g
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]
        val gameCard1 = GameCard("aaa", 1, BigDecimal(30), game, findMember, 1)
        val gameCard2 = GameCard("bbb", 2, BigDecimal(40), game, findMember, 2)
        gameCardRepository.save(gameCard1)
        gameCardRepository.save(gameCard2)
        val deleteDto = GameCardDeleteDto(gameCard2.id)
        // w
        gameCardService.delete(deleteDto)
        val assertMember = memberRepository.findById(findMember.id)

        // t
        Assertions.assertThat(assertMember.totalCardQuantity).isEqualTo(1)
    }

    @Test
    fun gameCard를_삭제할때_멤버의_레벨이_바뀐다()  {
        // g
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val findMember = memberRepository.findByNameStartsWith(name = "aaaa", sort)[0]

        val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]
        val gameCard1 = GameCard("aaa", 1, BigDecimal(30), game, findMember, 1)

        gameCardRepository.save(gameCard1)
        val deleteDto = GameCardDeleteDto(gameCard1.id)
        // w
        gameCardService.delete(deleteDto)
        val assertMember = memberRepository.findById(findMember.id)

        // t
        Assertions.assertThat(assertMember.level).isEqualTo(Level.Bronze)
    }
}