package com.example.demo.unittest.member.controller

import com.example.demo.exception.exception.DatePastException
import com.example.demo.member.Member
import com.example.demo.member.controller.MemberControllerImpl
import com.example.demo.member.dto.MemberInsertDto
import com.example.demo.member.dto.MemberResponseDto
import com.example.demo.member.dto.MemberUpdateDto
import com.example.demo.member.service.MemberService
import com.example.demo.mock.CustomDateFake
import com.example.demo.mock.TestConstant
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Sort
import java.time.LocalDate

class MemberControllerTest {
    private val memberService = mockk<MemberService>()
    private val memberController = MemberControllerImpl(memberService, CustomDateFake(TestConstant.TESTNOWDATE))

    @Test
    fun 전체조회에서_joinDate의_역순으로_정렬한다()  {
        // g
        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val memberList = mutableListOf<Member>()
        memberList.add(
            Member(
                "aaa",
                "aa@naver.com",
                CustomDateFake(TestConstant.TESTNOWDATE).now().minusDays(2),
                id = 1,
            ),
        )
        memberList.add(
            Member(
                "bbb",
                "bb@naver.com",
                CustomDateFake(TestConstant.TESTNOWDATE).now().minusDays(1),
                id = 2,
            ),
        )
        memberList.add(
            Member(
                "ccc",
                "cc@naver.com",
                CustomDateFake(TestConstant.TESTNOWDATE).now(),
                id = 3,
            ),
        )
        every { memberService.findDynamicSearchSortBy(sort) } returns memberList
        // w

        val findAllMember = memberController.findAll(name = null, level = null).data
        // t
        Assertions.assertThat(findAllMember.size).isEqualTo(3)
    }

    @Test
    fun 전체조회에서_MemberResponseDto로_반환한다()  {
        // g
        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val memberList = mutableListOf<Member>()
        memberList.add(
            Member(
                "aaa",
                "aa@naver.com",
                CustomDateFake(TestConstant.TESTNOWDATE).now().minusDays(2),
                id = 1,
            ),
        )
        memberList.add(
            Member(
                "bbb",
                "bb@naver.com",
                CustomDateFake(TestConstant.TESTNOWDATE).now().minusDays(1),
                id = 2,
            ),
        )
        memberList.add(
            Member(
                "ccc",
                "cc@naver.com",
                CustomDateFake(TestConstant.TESTNOWDATE).now(),
                id = 3,
            ),
        )
        every { memberService.findDynamicSearchSortBy(sort) } returns memberList
        // w
        val findMember = memberController.findAll(name = null, level = null).data[0]
        // t
        Assertions.assertThat(findMember).isInstanceOf(MemberResponseDto::class.java)
    }

    @Test
    fun member을_저장할때_MemberInsertDto의_시간이_과거1년안이면_성공한다()  {
        // g
        val memberInsertDto =
            MemberInsertDto(
                "aaaa",
                "aa@naver.com",
                LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(1),
            )
        every { memberService.save(memberInsertDto) } returns Unit
        // w,t
        memberController.save(memberInsertDto)
    }

    @Test
    fun member을_저장할때_MemberInsertDto의_시간이_과거1년밖이면_예외가_발생한다()  {
        // g
        val memberInsertDto =
            MemberInsertDto(
                "aaaa",
                "aa@naver.com",
                LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(400),
            )
        every { memberService.save(memberInsertDto) } returns Unit
        // w,t
        Assertions.assertThatThrownBy { memberController.save(memberInsertDto) }
            .isInstanceOf(DatePastException::class.java)
    }

    @Test
    fun member을_수정할때_MemberUpdateDto의_시간이_과거1년안이면_성공한다()  {
        // g
        val memberUpdateDto =
            MemberUpdateDto(
                "aaaa",
                "aa@naver.com",
                LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(1),
                1,
            )
        every { memberService.update(memberUpdateDto) } returns Unit
        // w,t
        memberController.update(1, memberUpdateDto)
    }

    @Test
    fun member을_수정할때_MemberUpdateDto의_시간이_과거1년밖이면_예외가_발생한다()  {
        // g
        val memberUpdateDto =
            MemberUpdateDto(
                "aaaa",
                "aa@naver.com",
                LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(400),
                1,
            )
        every { memberService.update(memberUpdateDto) } returns Unit
        // w,t
        Assertions.assertThatThrownBy { memberController.update(1, memberUpdateDto) }
            .isInstanceOf(DatePastException::class.java)
    }
}
