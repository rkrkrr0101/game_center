@file:Suppress("ktlint:standard:function-naming")

package com.example.demo.unit

import com.example.demo.constant.Level
import com.example.demo.game.Game
import com.example.demo.gamecard.GameCard
import com.example.demo.member.Member
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class MemberTest {
    @Test
    fun 정상적으로_카드를_추가할수_있다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "test1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val gameCard =
            GameCard(
                "testTitle1",
                30,
                BigDecimal(20.22).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            )

        member.addGameCard(gameCard)

        Assertions.assertThat(member.gameCardList.size).isEqualTo(1)
    }

    @Test
    fun 카드를_추가하면_자동으로_총가격과_갯수가_계산된다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "test1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)

        val gameCard1 =
            GameCard(
                "testTitle1",
                30,
                BigDecimal(20.22).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            )
        val gameCard2 =
            GameCard(
                "testTitle2",
                31,
                BigDecimal(20).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                2,
            )
        member.addGameCard(gameCard1)
        member.addGameCard(gameCard2)

        Assertions.assertThat(member.gameCardList.size).isEqualTo(2)
        Assertions.assertThat(member.totalCardPrice).isEqualTo(BigDecimal("40.22"))
        Assertions.assertThat(member.totalCardQuantity).isEqualTo(2)
    }

    @Test
    fun 카드가_한장도_없으면_브론즈레벨이다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "test1", email = "aa@naver.com", joinDate = testDate, id = 1)

        Assertions.assertThat(member.gameCardList.size).isEqualTo(0)
        Assertions.assertThat(member.level).isEqualTo(Level.Bronze)
    }

    @Test
    fun 무료카드만_가지고있으면_브론즈레벨이다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "test1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val gameCard1 =
            GameCard(
                "testTitle1",
                30,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            )
        val gameCard2 =
            GameCard(
                "testTitle2",
                31,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                2,
            )
        member.addGameCard(gameCard1)
        member.addGameCard(gameCard2)

        Assertions.assertThat(member.gameCardList.size).isEqualTo(2)
        Assertions.assertThat(member.level).isEqualTo(Level.Bronze)
    }

    @Test
    fun 유효카드를_한장이상_가지고있으면_실버레벨이다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "test1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val gameCard1 =
            GameCard(
                "testTitle1",
                30,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            )

        member.addGameCard(gameCard1)

        Assertions.assertThat(member.gameCardList.size).isEqualTo(1)
        Assertions.assertThat(member.level).isEqualTo(Level.Silver)
    }

    @Test
    fun 카드가_두종류이상이고_게임이_두종류이상이고_총합이_100달러가_넘으면_골드레벨이다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "test1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val gameCard1 =
            GameCard(
                "testTitle1",
                30,
                BigDecimal(20.22).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            )
        val gameCard2 =
            GameCard(
                "testTitle2",
                31,
                BigDecimal(80).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                2,
            )
        member.addGameCard(gameCard1)
        member.addGameCard(gameCard2)

        Assertions.assertThat(member.gameCardList.size).isEqualTo(2)
        Assertions.assertThat(member.totalCardPrice).isEqualTo(BigDecimal("100.22"))
        Assertions.assertThat(member.totalCardQuantity).isEqualTo(2)
        Assertions.assertThat(member.level).isEqualTo(Level.Gold)
    }

    @Test
    fun 유효카드가_네종류_이상이고_게임이_두종류_이상이면_골드레벨이다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "test1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val gameCard1 =
            GameCard(
                "testTitle1",
                30,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            )
        val gameCard2 =
            GameCard(
                "testTitle2",
                31,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                2,
            )
        val gameCard3 =
            GameCard(
                "testTitle3",
                32,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                3,
            )
        val gameCard4 =
            GameCard(
                "testTitle4",
                33,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                4,
            )
        member.addGameCard(gameCard1)
        member.addGameCard(gameCard2)
        member.addGameCard(gameCard3)
        member.addGameCard(gameCard4)

        Assertions.assertThat(member.gameCardList.size).isEqualTo(4)
        Assertions.assertThat(member.totalCardPrice).isEqualTo(BigDecimal("4.00"))
        Assertions.assertThat(member.totalCardQuantity).isEqualTo(4)
        Assertions.assertThat(member.level).isEqualTo(Level.Gold)
    }

    @Test
    fun 무료카드는_레벨계산에_포함되지않는다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "test1", email = "aa@naver.com", joinDate = testDate, id = 1)
        val pokeGame = Game("poke", 1)
        val elvGame = Game("elv", 2)
        val gameCard1 =
            GameCard(
                "testTitle1",
                30,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                pokeGame,
                member,
                1,
            )
        val gameCard2 =
            GameCard(
                "testTitle2",
                31,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                2,
            )
        val gameCard3 =
            GameCard(
                "testTitle3",
                32,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                3,
            )
        val gameCard4 =
            GameCard(
                "testTitle4",
                33,
                BigDecimal(0).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                4,
            )
        member.addGameCard(gameCard1)
        member.addGameCard(gameCard2)
        member.addGameCard(gameCard3)
        member.addGameCard(gameCard4)

        Assertions.assertThat(member.gameCardList.size).isEqualTo(4)
        Assertions.assertThat(member.totalCardPrice).isEqualTo(BigDecimal("0.00"))
        Assertions.assertThat(member.totalCardQuantity).isEqualTo(4)
        Assertions.assertThat(member.level).isEqualTo(Level.Bronze)
    }

    @Test
    fun 게임이_한종류면_골드레벨이_될수없다() {
        val testDate = LocalDate.parse("2020-11-01")
        val member = Member(name = "test1", email = "aa@naver.com", joinDate = testDate, id = 1)

        val elvGame = Game("elv", 2)
        val gameCard1 =
            GameCard(
                "testTitle1",
                30,
                BigDecimal(100).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                1,
            )
        val gameCard2 =
            GameCard(
                "testTitle2",
                31,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                2,
            )
        val gameCard3 =
            GameCard(
                "testTitle3",
                32,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                3,
            )
        val gameCard4 =
            GameCard(
                "testTitle4",
                33,
                BigDecimal(1).setScale(2, RoundingMode.HALF_UP),
                elvGame,
                member,
                4,
            )
        member.addGameCard(gameCard1)
        member.addGameCard(gameCard2)
        member.addGameCard(gameCard3)
        member.addGameCard(gameCard4)

        Assertions.assertThat(member.gameCardList.size).isEqualTo(4)
        Assertions.assertThat(member.totalCardPrice).isEqualTo(BigDecimal("103.00"))
        Assertions.assertThat(member.totalCardQuantity).isEqualTo(4)
        Assertions.assertThat(member.level).isEqualTo(Level.Silver)
    }
}
