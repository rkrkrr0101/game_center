package com.example.demo.unittest.gamecard.dto

import com.example.demo.game.Game
import com.example.demo.gamecard.GameCard
import com.example.demo.gamecard.dto.GameCardInsertDto
import com.example.demo.gamecard.dto.GameCardResponseDto
import com.example.demo.member.Member
import com.example.demo.mock.ClockFixedProviderFake
import com.example.demo.mock.CustomDateFake
import com.example.demo.mock.TestConstant
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class GameCardDtoTest {
    private val validator =
        Validation
            .byDefaultProvider()
            .configure()
            .clockProvider(ClockFixedProviderFake(TestConstant.TESTNOWDATETIME))
            .buildValidatorFactory()
            .validator

    @Test
    fun gameCardInsertDto로_밸리데이션을_통과할수_있다()  {
        val game = Game("pokemon", 1)
        val member = Member("aaa", "aa@naver.com", CustomDateFake(TestConstant.TESTNOWDATE).now(), id = 1)
        val insertDto = GameCardInsertDto("abcd", 1, BigDecimal(300), game.id, member.id)

        val validateResult: MutableSet<ConstraintViolation<GameCardInsertDto>> = validator.validate(insertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(0)
    }

    @Test
    fun gameCardInsertDto의_title이_영자릿수면_실패한다()  {
        val game = Game("pokemon", 1)
        val member = Member("aaa", "aa@naver.com", CustomDateFake(TestConstant.TESTNOWDATE).now(), id = 1)
        val insertDto = GameCardInsertDto("", 1, BigDecimal(300), game.id, member.id)

        val validateResult: MutableSet<ConstraintViolation<GameCardInsertDto>> = validator.validate(insertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun gameCardInsertDto의_title이_백자리이상이면_실패한다()  {
        val game = Game("pokemon", 1)
        val member = Member("aaa", "aa@naver.com", CustomDateFake(TestConstant.TESTNOWDATE).now(), id = 1)
        var nameString = ""
        for (i in 0..103) {
            nameString = nameString.plus("a")
        }
        val insertDto = GameCardInsertDto(nameString, 1, BigDecimal(300), game.id, member.id)

        val validateResult: MutableSet<ConstraintViolation<GameCardInsertDto>> = validator.validate(insertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun gameCardInsertDto의_title이_전체가_공백이면_실패한다()  {
        val game = Game("pokemon", 1)
        val member = Member("aaa", "aa@naver.com", CustomDateFake(TestConstant.TESTNOWDATE).now(), id = 1)
        val insertDto = GameCardInsertDto("     ", 1, BigDecimal(300), game.id, member.id)

        val validateResult: MutableSet<ConstraintViolation<GameCardInsertDto>> = validator.validate(insertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun gameCardInsertDto의_serialNo가_1보다작으면_실패한다()  {
        val game = Game("pokemon", 1)
        val member = Member("aaa", "aa@naver.com", CustomDateFake(TestConstant.TESTNOWDATE).now(), id = 1)
        val insertDto = GameCardInsertDto("abcd", 0, BigDecimal(300), game.id, member.id)

        val validateResult: MutableSet<ConstraintViolation<GameCardInsertDto>> = validator.validate(insertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun gameCardInsertDto의_price가_0보다_작으면_실패한다()  {
        val game = Game("pokemon", 1)
        val member = Member("aaa", "aa@naver.com", CustomDateFake(TestConstant.TESTNOWDATE).now(), id = 1)
        val insertDto = GameCardInsertDto("abcd", 1, BigDecimal(-1), game.id, member.id)

        val validateResult: MutableSet<ConstraintViolation<GameCardInsertDto>> = validator.validate(insertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun gameCardInsertDto의_price가_100000보다_크면_실패한다()  {
        val game = Game("pokemon", 1)
        val member = Member("aaa", "aa@naver.com", CustomDateFake(TestConstant.TESTNOWDATE).now(), id = 1)
        val insertDto = GameCardInsertDto("abcd", 1, BigDecimal(100001), game.id, member.id)

        val validateResult: MutableSet<ConstraintViolation<GameCardInsertDto>> = validator.validate(insertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun gameCardInsertDto의_price의_소수점이_3자리이상이면_2자리로_반올림한다()  {
        val game = Game("pokemon", 1)
        val member = Member("aaa", "aa@naver.com", CustomDateFake(TestConstant.TESTNOWDATE).now(), id = 1)
        val insertDto = GameCardInsertDto("abcd", 1, BigDecimal(3.3535), game.id, member.id)

        Assertions.assertThat(insertDto.price.toPlainString()).isEqualTo("3.35")
    }

    @Test
    fun gameCard로_gameCardResponseDto를_생성할수_있다()  {
        val game = Game("pokemon", 1)
        val member = Member("aaa", "aa@naver.com", CustomDateFake(TestConstant.TESTNOWDATE).now(), id = 1)
        val gameCard = GameCard("abcd", 1, BigDecimal(3.3535), game, member, 1)

        val responseDto = GameCardResponseDto.domainToDto(gameCard)

        Assertions.assertThat(responseDto.id).isEqualTo(gameCard.id)
    }
}
