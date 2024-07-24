@file:Suppress("ktlint:standard:no-wildcard-imports", "ktlint:standard:function-naming")

package com.example.demo.integration.service

import com.example.demo.constant.Level
import com.example.demo.domain.game.service.GameRepository
import com.example.demo.domain.gamecard.service.GameCardRepository
import com.example.demo.domain.member.Member
import com.example.demo.domain.member.dto.MemberDeleteDto
import com.example.demo.domain.member.dto.MemberInsertDto
import com.example.demo.domain.member.dto.MemberUpdateDto
import com.example.demo.domain.member.service.MemberRepository
import com.example.demo.domain.member.service.MemberService
import com.example.demo.exception.exception.EmailDuplicateException
import com.example.demo.mock.*
import com.example.demo.util.TestFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@Transactional
class MemberServiceTest
    @Autowired
    constructor(
        val gameCardRepository: GameCardRepository,
        val memberRepository: MemberRepository,
        val gameRepository: GameRepository,
    ) {
        val memberService =
            MemberService(
                memberRepository,
                gameCardRepository,
                listOf(SlackPortMock(), KafkaPortMock()),
            )

        @Test
        fun 동적쿼리에_이름과_레벨을_넣지않으면_전체를_리턴한다() {
            val member1 = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member2 = Member("abbb", "ab@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member3 = Member("accc", "ac@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member4 = Member("addd", "ad@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member5 = Member("aeee", "ae@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)
            memberRepository.save(member4)
            memberRepository.save(member5)
            val sort = Sort.by(Sort.Direction.ASC, "joinDate")

            val findMemberList = memberService.findDynamicSearchSortBy(sort)

            Assertions.assertThat(findMemberList.size).isEqualTo(5)
        }

        @Test
        fun 동적쿼리에_이름만_넣으면_이름을_like조회해서_리턴한다() {
            val member1 = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member2 = Member("aabb", "ab@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member3 = Member("accc", "ac@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member4 = Member("addd", "ad@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member5 = Member("aeee", "ae@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)
            memberRepository.save(member4)
            memberRepository.save(member5)
            val sort = Sort.by(Sort.Direction.ASC, "joinDate")

            val findMemberList = memberService.findDynamicSearchSortBy(sort, name = "aa")

            Assertions.assertThat(findMemberList.size).isEqualTo(2)
        }

        @Test
        fun 동적쿼리에_레벨만_넣으면_레벨로_조회해서_리턴한다() {
            val member1 =
                Member(
                    "aaaa",
                    "aa@naver.com",
                    LocalDate.parse(TestConstant.TESTNOWDATE),
                    3,
                    BigDecimal(300),
                    Level.Gold,
                )
            val member2 =
                Member(
                    "aabb",
                    "ab@naver.com",
                    LocalDate.parse(TestConstant.TESTNOWDATE),
                    3,
                    BigDecimal(300),
                    Level.Gold,
                )
            val member3 =
                Member(
                    "accc",
                    "ac@naver.com",
                    LocalDate.parse(TestConstant.TESTNOWDATE),
                    3,
                    BigDecimal(300),
                    Level.Silver,
                )
            val member4 = Member("addd", "ad@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member5 = Member("aeee", "ae@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)
            memberRepository.save(member4)
            memberRepository.save(member5)
            val sort = Sort.by(Sort.Direction.ASC, "joinDate")

            val findMemberList = memberService.findDynamicSearchSortBy(sort, level = Level.Gold)

            Assertions.assertThat(findMemberList.size).isEqualTo(2)
        }

        @Test
        fun 동적쿼리에_이름과_레벨을_넣으면_이름을_like조회하고_레벨로_조회해서_리턴한다() {
            val member1 =
                Member(
                    "aaaa",
                    "aa@naver.com",
                    LocalDate.parse(TestConstant.TESTNOWDATE),
                    3,
                    BigDecimal(300),
                    Level.Gold,
                )
            val member2 =
                Member(
                    "aabb",
                    "ab@naver.com",
                    LocalDate.parse(TestConstant.TESTNOWDATE),
                    3,
                    BigDecimal(300),
                    Level.Silver,
                )
            val member3 =
                Member(
                    "accc",
                    "ac@naver.com",
                    LocalDate.parse(TestConstant.TESTNOWDATE),
                    3,
                    BigDecimal(300),
                    Level.Silver,
                )
            val member4 = Member("addd", "ad@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val member5 = Member("aeee", "ae@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)
            memberRepository.save(member4)
            memberRepository.save(member5)
            val sort = Sort.by(Sort.Direction.ASC, "joinDate")

            val findMemberList = memberService.findDynamicSearchSortBy(sort, name = "aa", level = Level.Gold)

            Assertions.assertThat(findMemberList.size).isEqualTo(1)
        }

        @Test
        fun 멤버의_id로_조회할수_있다() {
            val member1 =
                Member(
                    "aaaa",
                    "aa@naver.com",
                    LocalDate.parse(TestConstant.TESTNOWDATE),
                    3,
                    BigDecimal(300),
                    Level.Gold,
                )
            val member2 =
                Member(
                    "aabb",
                    "ab@naver.com",
                    LocalDate.parse(TestConstant.TESTNOWDATE),
                    3,
                    BigDecimal(300),
                    Level.Silver,
                )
            val member3 =
                Member(
                    "accc",
                    "ac@naver.com",
                    LocalDate.parse(TestConstant.TESTNOWDATE),
                    3,
                    BigDecimal(300),
                    Level.Silver,
                )
            memberRepository.save(member1)
            memberRepository.save(member2)
            memberRepository.save(member3)

            val findMember = memberService.findById(member1.id)

            Assertions.assertThat(findMember.id).isEqualTo(member1.id)
        }

        @Test
        fun member의_Id가_없다면_예외가_발생한다() {
            Assertions.assertThatThrownBy { memberService.findById(100) }
                .isInstanceOf(JpaObjectRetrievalFailureException::class.java)
        }

        @Test
        fun member를_저장할수_있다() {
            val insertDto = MemberInsertDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            val sort = Sort.by(Sort.Direction.ASC, "joinDate")

            memberService.save(insertDto)
            val findMember = memberService.findDynamicSearchSortBy(sort)[0]

            Assertions.assertThat(findMember.email).isEqualTo("aa@naver.com")
        }

        @Test
        fun member를_save할때_이메일이_중복이면_예외가_발생한다() {
            val insertDto = MemberInsertDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberService.save(insertDto)

            val duplicateDto = MemberInsertDto("aabb", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            duplicateDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)
            duplicateDto.checkJoinDateYear()

            Assertions.assertThatThrownBy { memberService.save(duplicateDto) }
                .isInstanceOf(EmailDuplicateException::class.java)
        }

        @Test
        fun member를_update할수있다() {
            // g
            val insertDto1 = MemberInsertDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberService.save(insertDto1)
            val insertDto2 = MemberInsertDto("aabb", "aabb@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberService.save(insertDto2)
            val sort = Sort.by(Sort.Direction.ASC, "joinDate")
            val findMember = memberService.findDynamicSearchSortBy(sort, name = "aaaa")[0]
            // w

            val updateDto =
                MemberUpdateDto(
                    findMember.name,
                    "aabbcc@naver.com",
                    findMember.joinDate,
                    findMember.id,
                )
            memberService.update(updateDto)
            val findEmailMember = memberRepository.findByEmail("aabbcc@naver.com")
            // t
            Assertions.assertThat(findEmailMember!!.id).isEqualTo(findMember.id)
        }

        @Test
        fun member를_update할때_이메일이_중복이면_예외가_발생한다() {
            // g
            val insertDto1 = MemberInsertDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberService.save(insertDto1)

            val insertDto2 = MemberInsertDto("aabb", "aabb@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberService.save(insertDto2)
            val sort = Sort.by(Sort.Direction.ASC, "joinDate")
            val findMember = memberService.findDynamicSearchSortBy(sort, name = "aaaa")[0]
            // w,t

            val updateDto =
                MemberUpdateDto(
                    findMember.name,
                    "aabb@naver.com",
                    findMember.joinDate,
                    findMember.id,
                )

            Assertions.assertThatThrownBy { memberService.update(updateDto) }
                .isInstanceOf(EmailDuplicateException::class.java)
        }

        @Test
        fun member를_update할때_이메일이_원래사용하던_이메일일경우_정상처리된다() {
            // g
            val insertDto1 = MemberInsertDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberService.save(insertDto1)

            val sort = Sort.by(Sort.Direction.ASC, "joinDate")
            val findMember = memberService.findDynamicSearchSortBy(sort)[0]
            val updateDto =
                MemberUpdateDto(
                    "abcd",
                    "aa@naver.com",
                    findMember.joinDate,
                    findMember.id,
                )
            // w,t

            memberService.update(updateDto)
            val resMember = memberService.findDynamicSearchSortBy(sort)[0]
            Assertions.assertThat(resMember.id).isEqualTo(findMember.id)
            Assertions.assertThat(resMember.name).isEqualTo(updateDto.name)
            Assertions.assertThat(resMember.email).isEqualTo(updateDto.email)
        }

        @Test
        fun member를_삭제할수_있다() {
            // g
            val insertDto1 = MemberInsertDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberService.save(insertDto1)
            val sort = Sort.by(Sort.Direction.ASC, "joinDate")
            val findMember = memberService.findDynamicSearchSortBy(sort, name = "aaaa")[0]
            val deleteDto = MemberDeleteDto(findMember.id)
            // w
            memberService.delete(deleteDto)
            val memberList = memberService.findDynamicSearchSortBy(sort, name = "aaaa")
            // t
            Assertions.assertThat(memberList.size).isEqualTo(0)
        }

        @Test
        fun member를_삭제하면_member의_게임카드도_삭제된다() {
            // g
            val insertDto1 = MemberInsertDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
            memberService.save(insertDto1)

            val sort = Sort.by(Sort.Direction.ASC, "joinDate")
            val member = memberService.findDynamicSearchSortBy(sort, name = "aaaa")[0]

            val game = gameRepository.findAll().filter { it.title == "pokemon" }[0]
            val gameCard1 = TestFactory.createGameCard("aaa", 1, BigDecimal(30), game, member)
            val gameCard2 = TestFactory.createGameCard("bbb", 2, BigDecimal(40), game, member)
            member.addGameCard(gameCard1)
            member.addGameCard(gameCard2)

            val findGameCardId = gameCardRepository.findByMember(member)[0].id
            val deleteDto = MemberDeleteDto(member.id)
            // w
            memberService.delete(deleteDto)
            val memberList = memberService.findDynamicSearchSortBy(sort, name = "aaaa")

            // t
            Assertions.assertThat(memberList.size).isEqualTo(0)
            Assertions.assertThatThrownBy { gameCardRepository.findById(findGameCardId) }
                .isInstanceOf(JpaObjectRetrievalFailureException::class.java)
        }
    }
