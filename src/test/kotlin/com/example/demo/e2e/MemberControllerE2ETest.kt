@file:Suppress("ktlint:standard:function-naming")

package com.example.demo.e2e

import com.example.demo.constant.Level
import com.example.demo.customdate.CustomDate
import com.example.demo.domain.member.Member
import com.example.demo.domain.member.repository.MemberRepository
import com.example.demo.mock.CustomDateFake
import com.example.demo.mock.TestConstant
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerE2ETest
    @Autowired
    constructor(
        val mvc: MockMvc,
        val memberRepository: MemberRepository,
        val customDate: CustomDate,
    ) {
        val om = ObjectMapper()

        private fun status() = MockMvcResultMatchers.status()

        private fun jsonPath(key: String) = MockMvcResultMatchers.jsonPath(key)

        @Test
        fun 파라미터없이_던지면_전체조회를_한다() {
            val member1 =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val member2 =
                Member(
                    "bb",
                    "bb@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val member3 =
                Member(
                    "cc",
                    "cc@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)

            mvc.perform(MockMvcRequestBuilders.get("/member/all"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.length()").value(3))
        }

        @Test
        fun name만_넣고_던지면_name_left_like조회를_한다() {
            val member1 =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val member2 =
                Member(
                    "bb",
                    "bb@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val member3 =
                Member(
                    "cc",
                    "cc@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)

            mvc.perform(MockMvcRequestBuilders.get("/member/all?name=aa"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.length()").value(1))
        }

        @Test
        fun 레벨만_넣고_던지면_레벨조회를_한다() {
            val member1 =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val member2 =
                Member(
                    "bb",
                    "bb@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val member3 =
                Member(
                    "cc",
                    "cc@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)

            mvc.perform(MockMvcRequestBuilders.get("/member/all?level=Bronze"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.length()").value(3))
        }

        @Test
        fun name과_레벨을_같이_던지면_둘을_and_해서_조회를_한다() {
            val member1 =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val member2 =
                Member(
                    "aabb",
                    "bb@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                    level = Level.Silver,
                )
            val member3 =
                Member(
                    "aacc",
                    "cc@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                    level = Level.Gold,
                )
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)

            mvc.perform(MockMvcRequestBuilders.get("/member/all?level=Bronze&name=aa"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.length()").value(1))
        }

        @Test
        fun id를_가지고_단일멤버조회를_할수있다() {
            val member1 =
                Member(
                    "aa",
                    "aa@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val member2 =
                Member(
                    "aabb",
                    "bb@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            val member3 =
                Member(
                    "aacc",
                    "cc@naver.com",
                    CustomDateFake(TestConstant.TESTNOWDATE).now(),
                )
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)

            mvc.perform(MockMvcRequestBuilders.get("""/member/${member1.id}"""))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.id").value(member1.id))
        }

        @Test
        fun 해당되는_id의_멤버가_없으면_단일멤버조회시_httpcode400이_발생한다() {
            mvc.perform(MockMvcRequestBuilders.get("""/member/${100}"""))
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_저장할수_있다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "daqda"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)
            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isOk)
        }

        @Test
        fun 멤버를_저장할때_responseBody를_입력하지않으면_httpcode400이_발생한다() {
            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json"),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_저장할때_responseBody를_문법에_맞지않게_작성하면_httpcode400이_발생한다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "daqda"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            var reqBodyJson = om.writeValueAsString(jsonMap)
            reqBodyJson = "`" + reqBodyJson

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_저장할때_name이_한자리이하면_httpcode400이_발생한다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "a"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_저장할때_name이_백자리이상이면_httpcode400이_발생한다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            var tempName = ""
            for (i in 0..105) {
                tempName += "a"
            }
            jsonMap["name"] = tempName
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_저장할때_name이_공백으로만이루어지면_httpcode400이_발생한다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "     "
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_저장할때_email이_이메일형식이아니면_httpcode400이_발생한다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = "adada"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_저장할때_email이_공백이면_httpcode400이_발생한다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = ""
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_저장할때_joinTime이_미래면_httpcode400이_발생한다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().plusYears(1).format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_저장할때_joinTime이_과거1년이전이면_httpcode400이_발생한다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().minusYears(2).format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.post("""/member/save""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        // ㅇㅇ
        @Test
        fun 멤버를_업데이트할수_있다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isOk)
        }

        @Test
        fun 멤버를_업데이트할때_responseBody를_입력하지않으면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json"),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_업데이트할때_responseBody를_문법에_맞지않게_작성하면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            var reqBodyJson = om.writeValueAsString(jsonMap)
            reqBodyJson = "'" + reqBodyJson

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_업데이트할때_name이_한자리이하면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "a"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_업데이트할때_name이_백자리이상이면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            var tempName = ""
            for (i in 0..105) {
                tempName += "a"
            }
            jsonMap["name"] = tempName
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_업데이트할때_name이_공백으로만이루어지면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "    "
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_업데이트할때_email이_이메일형식이아니면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = "adada"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_업데이트할때_email이_공백이면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = ""
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_업데이트할때_joinTime이_미래면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().plusYears(1).format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_업데이트할때_joinTime이_과거1년이전이면_httpcode400이_발생한다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().minusYears(2).format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${member.id}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_업데이트할때_해당되는_id의_멤버가_없으면_httpcode400이_발생한다() {
            val format = DateTimeFormatter.ofPattern("yyyyMMdd")
            val nowDate = customDate.now().format(format)
            val jsonMap = hashMapOf<String, String>()
            jsonMap["name"] = "aabc"
            jsonMap["email"] = "aa@aqa.a"
            jsonMap["joinDate"] = nowDate
            val reqBodyJson = om.writeValueAsString(jsonMap)

            // w,t
            mvc.perform(
                MockMvcRequestBuilders.put("""/member/${100}""")
                    .contentType("application/json")
                    .content(reqBodyJson),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun 멤버를_삭제할수_있다() {
            val member =
                Member(
                    "aa",
                    "aa@naver.com",
                    customDate.now(),
                )
            memberRepository.save(member)
            // w,t
            mvc.perform(
                MockMvcRequestBuilders.delete("""/member/${member.id}"""),
            )
                .andExpect(status().isOk)
        }

        @Test
        fun 해당되는_id의_멤버가_없으면_삭제시_httpcode400이_발생한다() {
            mvc.perform(
                MockMvcRequestBuilders.delete("""/member/${100}"""),
            )
                .andExpect(status().isBadRequest)
        }
    }
