@file:Suppress("ktlint:standard:function-naming")

package com.example.demo.e2e

import com.example.demo.domain.game.repository.GameRepository
import com.example.demo.domain.gamecard.GameCard
import com.example.demo.domain.gamecard.repository.GameCardRepository
import com.example.demo.domain.member.Member
import com.example.demo.domain.member.repository.MemberRepository
import com.example.demo.mock.CustomDateFake
import com.example.demo.mock.TestConstant
import com.example.demo.util.customdate.CustomDate
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GameCardControllerE2ETest
    @Autowired
    constructor(
        val mvc: MockMvc,
        val memberRepository: MemberRepository,
        val gameCardRepository: GameCardRepository,
        val gameRepository: GameRepository,
        val customDate: CustomDate,
    ) {
        val om = ObjectMapper()

        private fun status() = MockMvcResultMatchers.status()

        private fun jsonPath(key: String) = MockMvcResultMatchers.jsonPath(key)

        @Test
        fun 멤버의_id로_게임카드를_조회할수_있다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val game = gameRepository.findAll()[0]
            val gameCard1 = GameCard("aaa", 12, BigDecimal(30), game, member)
            val gameCard2 = GameCard("bbb", 123, BigDecimal(30), game, member)
            val gameCard3 = GameCard("ccc", 1234, BigDecimal(30), game, member)
            member.addGameCard(gameCard1)
            member.addGameCard(gameCard2)
            member.addGameCard(gameCard3)

            memberRepository.save(member)

            // w,t
            mvc.perform(MockMvcRequestBuilders.get("""/gamecard/all?memberId=${member.id}"""))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.length()").value(3))
        }

        @Test
        fun 해당되는_id의_멤버가_없으면_멤버의_id로_게임카드를_조회시_httpcode400이_발생한다() {
            mvc.perform(MockMvcRequestBuilders.get("""/gamecard/all?memberId=${100}"""))
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 게임카드를_저장할수_있다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member)
            val game = gameRepository.findAll()[0]
            val jsonMap = hashMapOf<String, String>()
            jsonMap["title"] = "aaa"
            jsonMap["serialNo"] = "111"
            jsonMap["price"] = "30"
            jsonMap["gameId"] = game.id.toString()
            jsonMap["memberId"] = member.id.toString()
            val reqBodyJson = om.writeValueAsString(jsonMap)

            mvc.perform(
                MockMvcRequestBuilders.post("""/gamecard/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isOk)
        }

        @Test
        fun 게임카드를_저장할때_responseBody를_입력하지않으면_httpcode400이_발생한다() {
            mvc.perform(
                MockMvcRequestBuilders.post("""/gamecard/save""")
                    .contentType("application/json"),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 게임카드를_저장할때_responseBody를_문법에_맞지않게_작성하면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member)
            val game = gameRepository.findAll()[0]
            val jsonMap = hashMapOf<String, String>()
            jsonMap["title"] = "aaa"
            jsonMap["serialNo"] = "111"
            jsonMap["price"] = "30"
            jsonMap["gameId"] = game.id.toString()
            jsonMap["memberId"] = member.id.toString()
            var reqBodyJson = om.writeValueAsString(jsonMap)
            reqBodyJson = "'" + reqBodyJson

            mvc.perform(
                MockMvcRequestBuilders.post("""/gamecard/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 게임카드를_저장할때_title이_영자리이하면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member)
            val game = gameRepository.findAll()[0]
            val jsonMap = hashMapOf<String, String>()
            jsonMap["title"] = ""
            jsonMap["serialNo"] = "111"
            jsonMap["price"] = "30"
            jsonMap["gameId"] = game.id.toString()
            jsonMap["memberId"] = member.id.toString()
            val reqBodyJson = om.writeValueAsString(jsonMap)

            mvc.perform(
                MockMvcRequestBuilders.post("""/gamecard/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 게임카드를_저장할때_title이_전체가_공백이면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member)
            val game = gameRepository.findAll()[0]
            val jsonMap = hashMapOf<String, String>()
            jsonMap["title"] = "   "
            jsonMap["serialNo"] = "111"
            jsonMap["price"] = "30"
            jsonMap["gameId"] = game.id.toString()
            jsonMap["memberId"] = member.id.toString()
            val reqBodyJson = om.writeValueAsString(jsonMap)

            mvc.perform(
                MockMvcRequestBuilders.post("""/gamecard/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 게임카드를_저장할때_serialNo가_0이하면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member)
            val game = gameRepository.findAll()[0]
            val jsonMap = hashMapOf<String, String>()
            jsonMap["title"] = "aaa"
            jsonMap["serialNo"] = "0"
            jsonMap["price"] = "30"
            jsonMap["gameId"] = game.id.toString()
            jsonMap["memberId"] = member.id.toString()
            val reqBodyJson = om.writeValueAsString(jsonMap)

            mvc.perform(
                MockMvcRequestBuilders.post("""/gamecard/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 게임카드를_저장할때_price가_음수면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member)
            val game = gameRepository.findAll()[0]
            val jsonMap = hashMapOf<String, String>()
            jsonMap["title"] = "aaa"
            jsonMap["serialNo"] = "111"
            jsonMap["price"] = "-10"
            jsonMap["gameId"] = game.id.toString()
            jsonMap["memberId"] = member.id.toString()
            val reqBodyJson = om.writeValueAsString(jsonMap)

            mvc.perform(
                MockMvcRequestBuilders.post("""/gamecard/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 게임카드를_저장할때_price가_100000초과면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member)
            val game = gameRepository.findAll()[0]
            val jsonMap = hashMapOf<String, String>()
            jsonMap["title"] = "aaa"
            jsonMap["serialNo"] = "111"
            jsonMap["price"] = "100001"
            jsonMap["gameId"] = game.id.toString()
            jsonMap["memberId"] = member.id.toString()
            val reqBodyJson = om.writeValueAsString(jsonMap)

            mvc.perform(
                MockMvcRequestBuilders.post("""/gamecard/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 게임카드를_저장할때_price가_소수점자릿수3자리이상이면_2자리로_반올림한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member)
            val game = gameRepository.findAll()[0]
            val jsonMap = hashMapOf<String, String>()
            jsonMap["title"] = "aaa"
            jsonMap["serialNo"] = "111"
            jsonMap["price"] = "30.1313"
            jsonMap["gameId"] = game.id.toString()
            jsonMap["memberId"] = member.id.toString()
            val reqBodyJson = om.writeValueAsString(jsonMap)
            // w
            mvc.perform(
                MockMvcRequestBuilders.post("""/gamecard/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isOk)
            val findGameCard = gameCardRepository.findByMember(member)[0]
            // t

            Assertions.assertThat(findGameCard.price.toPlainString()).isEqualTo("30.13")
        }

        @Test
        fun 게임카드의_id로_게임카드를_삭제할수_있다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val game = gameRepository.findAll()[0]
            val gameCard = GameCard("aaa", 12, BigDecimal(30), game, member)
            member.addGameCard(gameCard)
            memberRepository.save(member)

            // w
            mvc.perform(MockMvcRequestBuilders.delete("""/gamecard/${gameCard.id}"""))
                .andExpect(status().isOk)
            val gameCardList = gameCardRepository.findByMember(member)
            Assertions.assertThat(gameCardList.size).isEqualTo(0)
        }

        @Test
        fun 삭제시_해당되는_id의_게임카드가_없으면_httpcode400이_발생한다() {
            mvc.perform(MockMvcRequestBuilders.delete("""/gamecard/${100}"""))
                .andExpect(status().isBadRequest)
        }
    }
