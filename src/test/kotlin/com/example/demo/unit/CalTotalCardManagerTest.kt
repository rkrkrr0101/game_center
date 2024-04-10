@file:Suppress("ktlint:standard:function-naming")

package com.example.demo.unit

import com.example.demo.caltotalcardmanager.CalTotalCardManager
import com.example.demo.constant.Level
import com.example.demo.game.Game
import com.example.demo.gamecard.GameCard
import com.example.demo.member.Member
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class CalTotalCardManagerTest {
    private val calTotalCardManager = CalTotalCardManager()

    @Test
    fun 정상적으로_카드의_수량과_가격을_계산할수_있다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "테스트1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val cardList = mutableListOf<GameCard>()
        cardList.add(
            GameCard(
                "testTitle1",
                30,
                BigDecimal(20.22).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle2",
                31,
                BigDecimal(20).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                2,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle3",
                32,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                3,
            ),
        )

        val (quantity, price) = calTotalCardManager.calTotalCardQuantityAndPrice(cardList)

        Assertions.assertThat(quantity).isEqualTo(3)
        Assertions.assertThat(price).isEqualTo(BigDecimal("40.22"))
    }

    @Test
    fun 정상적으로_실버_레벨을_계산할수_있다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "테스트1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val cardList = mutableListOf<GameCard>()
        cardList.add(
            GameCard(
                "testTitle1",
                30,
                BigDecimal(20.22).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle2",
                32,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                3,
            ),
        )

        val calLevel = calTotalCardManager.calLevel(cardList)
        Assertions.assertThat(calLevel).isEqualTo(Level.Silver)
    }

    @Test
    fun 카드가_두종류_이상이고_게임이_두종류_이상이고_100달러가_넘으면_골드레벨이다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "테스트1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val cardList = mutableListOf<GameCard>()
        cardList.add(
            GameCard(
                "testTitle1",
                30,
                BigDecimal(80).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle2",
                32,
                BigDecimal(100).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                3,
            ),
        )

        val calLevel = calTotalCardManager.calLevel(cardList)
        Assertions.assertThat(calLevel).isEqualTo(Level.Gold)
    }

    @Test
    fun 카드가_네종류_이상이고_게임이_두종류_이상이면_골드레벨이다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "테스트1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val cardList = mutableListOf<GameCard>()
        cardList.add(
            GameCard(
                "testTitle1",
                30,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle2",
                31,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                2,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle3",
                32,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                3,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle4",
                33,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                4,
            ),
        )

        val calLevel = calTotalCardManager.calLevel(cardList)
        Assertions.assertThat(calLevel).isEqualTo(Level.Gold)
    }

    @Test
    fun 카드가_없으면_브론즈_레벨이다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "테스트1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val cardList = mutableListOf<GameCard>()

        val calLevel = calTotalCardManager.calLevel(cardList)
        Assertions.assertThat(calLevel).isEqualTo(Level.Bronze)
    }

    @Test
    fun 아무리_카드가_많아도_0달러면_레벨계산에_포함되지않는다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "테스트1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val cardList = mutableListOf<GameCard>()
        cardList.add(
            GameCard(
                "testTitle1",
                30,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle2",
                32,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                2,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle3",
                33,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                3,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle4",
                34,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                4,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle5",
                35,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                5,
            ),
        )

        val calLevel = calTotalCardManager.calLevel(cardList)

        Assertions.assertThat(calLevel).isEqualTo(Level.Bronze)
    }

    @Test
    fun 아무리_카드가_비싸고_많아도_게임종류가_하나면_골드가_될수_없다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "테스트1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val cardList = mutableListOf<GameCard>()
        cardList.add(
            GameCard(
                "testTitle1",
                30,
                BigDecimal(300).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                1,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle2",
                32,
                BigDecimal(300).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                2,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle3",
                33,
                BigDecimal(300).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                3,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle4",
                34,
                BigDecimal(300).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                4,
            ),
        )
        cardList.add(
            GameCard(
                "testTitle5",
                35,
                BigDecimal(300).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                5,
            ),
        )

        val calLevel = calTotalCardManager.calLevel(cardList)

        Assertions.assertThat(calLevel).isEqualTo(Level.Silver)
    }
}
