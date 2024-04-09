package com.example.demo.unittest.gamecard.controller

import com.example.demo.game.Game
import com.example.demo.gamecard.GameCard
import com.example.demo.gamecard.controller.GameCardControllerImpl
import com.example.demo.gamecard.dto.GameCardResponseDto
import com.example.demo.gamecard.service.GameCardService
import com.example.demo.member.Member
import com.example.demo.mock.CustomDateFake
import com.example.demo.mock.TestConstant
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class GameCardControllerTest() {
    private val gameCardService = mockk<GameCardService>()
    private val gameCardController = GameCardControllerImpl(gameCardService)

    @Test
    fun 멤버로_게임카드_검색시에_GameResponseDto로_반환한다()  {
        // g
        val member =
            Member(
                "aaa",
                "aa@naver.com",
                CustomDateFake(TestConstant.TESTNOWDATE).now().minusDays(2),
                id = 1,
            )
        val game = Game("poke", 1)
        val gameCardList = mutableListOf<GameCard>()
        gameCardList.add(GameCard("aa", 12, BigDecimal(30), game, member, 1))
        gameCardList.add(GameCard("ab", 123, BigDecimal(31), game, member, 2))
        gameCardList.add(GameCard("ac", 1234, BigDecimal(32), game, member, 3))
        every { gameCardService.findByMember(member.id) } returns gameCardList
        // w
        val findGameCard = gameCardController.findByMember(member.id).data[0]

        Assertions.assertThat(findGameCard).isInstanceOf(GameCardResponseDto::class.java)
    }
}
