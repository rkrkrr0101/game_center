package com.example.demo.unittest.member.dto

import com.example.demo.constant.Level
import com.example.demo.exception.exception.DatePastException
import com.example.demo.member.Member
import com.example.demo.member.dto.MemberInsertDto
import com.example.demo.member.dto.MemberResponseDto
import com.example.demo.member.dto.MemberUpdateDto
import com.example.demo.mock.ClockFixedProviderFake
import com.example.demo.mock.CustomDateFake
import com.example.demo.mock.TestConstant
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class MemberDtoTest {
    private val validator =
        Validation
            .byDefaultProvider()
            .configure()
            .clockProvider(ClockFixedProviderFake(TestConstant.TESTNOWDATETIME))
            .buildValidatorFactory()
            .validator

    @Test
    fun MemberInsertDto로_validation을_통과할수_있다()  {
        val memberInsertDto = MemberInsertDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberInsertDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        memberInsertDto.checkJoinDateYear()
        val validateResult: MutableSet<ConstraintViolation<MemberInsertDto>> = validator.validate(memberInsertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(0)
    }

    @Test
    fun MemberInsertDto로_member을_생성할수_있다()  {
        val memberInsertDto = MemberInsertDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberInsertDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val member = memberInsertDto.dtoToDomain()

        Assertions.assertThat(member.name).isEqualTo("aaaa")
    }

    @Test
    fun MemberInsertDto의_name이_한자리이하면_실패한다()  {
        val memberInsertDto = MemberInsertDto("a", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberInsertDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberInsertDto>> = validator.validate(memberInsertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberInsertDto의_name이_백자리이상이면_실패한다()  {
        var nameString = ""
        for (i in 0..103) {
            nameString = nameString.plus("a")
        }
        val memberInsertDto = MemberInsertDto(nameString, "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberInsertDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberInsertDto>> = validator.validate(memberInsertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberInsertDto의_name이_공백으로만_이루어지면_실패한다()  {
        // 공백5자리
        val memberInsertDto = MemberInsertDto("     ", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberInsertDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberInsertDto>> = validator.validate(memberInsertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberInsertDto의_email이_이메일형식이_아니면_실패한다()  {
        val memberInsertDto = MemberInsertDto("aaaa", "aanaver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberInsertDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberInsertDto>> = validator.validate(memberInsertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberInsertDto의_email이_공백이면_실패한다()  {
        val memberInsertDto = MemberInsertDto("aaaa", "", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberInsertDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberInsertDto>> = validator.validate(memberInsertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberInsertDto의_joinTime이_미래면_실패한다()  {
        val memberInsertDto =
            MemberInsertDto(
                "aaaa",
                "aa@naver.com",
                LocalDate.parse(TestConstant.TESTNOWDATE).plusDays(5),
            )
        memberInsertDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberInsertDto>> = validator.validate(memberInsertDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberInsertDto의_joinTime이_과거1년이전이면_실패한다()  {
        val memberInsertDto =
            MemberInsertDto(
                "aaaa",
                "aa@naver.com",
                LocalDate.parse(TestConstant.TESTNOWDATE).minusYears(2),
            )
        memberInsertDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)
        Assertions.assertThatThrownBy {
            memberInsertDto.checkJoinDateYear()
        }.isInstanceOf(DatePastException::class.java)
    }

    @Test
    fun MemberUpdateDto로_validation을_통과할수_있다()  {
        val memberUpdateDto = MemberUpdateDto("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberUpdateDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        memberUpdateDto.checkJoinDateYear()
        val validateResult: MutableSet<ConstraintViolation<MemberUpdateDto>> = validator.validate(memberUpdateDto)

        Assertions.assertThat(validateResult.size).isEqualTo(0)
    }

    @Test
    fun MemberUpdateDto의_name이_한자리이하면_실패한다()  {
        val memberUpdateDto = MemberUpdateDto("a", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberUpdateDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberUpdateDto>> = validator.validate(memberUpdateDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberUpdateDto의_name이_백자리이상이면_실패한다()  {
        var nameString = ""
        for (i in 0..103) {
            nameString = nameString.plus("a")
        }
        val memberUpdateDto = MemberUpdateDto(nameString, "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberUpdateDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberUpdateDto>> = validator.validate(memberUpdateDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberUpdateDto의_name이_공백으로만_이루어지면_실패한다()  {
        // 공백5자리
        val memberUpdateDto = MemberUpdateDto("     ", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberUpdateDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberUpdateDto>> = validator.validate(memberUpdateDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberUpdateDto의_email이_이메일형식이_아니면_실패한다()  {
        val memberUpdateDto = MemberUpdateDto("aaaa", "aanaver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberUpdateDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberUpdateDto>> = validator.validate(memberUpdateDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberUpdateDto의_email이_공백이면_실패한다()  {
        val memberUpdateDto = MemberUpdateDto("aaaa", "", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberUpdateDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberUpdateDto>> = validator.validate(memberUpdateDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberUpdateDto의_joinTime이_미래면_실패한다()  {
        val memberUpdateDto =
            MemberUpdateDto(
                "aaaa",
                "aa@naver.com",
                LocalDate.parse(TestConstant.TESTNOWDATE).plusDays(5),
            )
        memberUpdateDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)

        val validateResult: MutableSet<ConstraintViolation<MemberUpdateDto>> = validator.validate(memberUpdateDto)

        Assertions.assertThat(validateResult.size).isEqualTo(1)
    }

    @Test
    fun MemberUpdateDto의_joinTime이_과거1년이전이면_실패한다()  {
        val memberUpdateDto =
            MemberUpdateDto(
                "aaaa",
                "aa@naver.com",
                LocalDate.parse(TestConstant.TESTNOWDATE).minusYears(2),
            )
        memberUpdateDto.customDate = CustomDateFake(TestConstant.TESTNOWDATE)
        Assertions.assertThatThrownBy {
            memberUpdateDto.checkJoinDateYear()
        }.isInstanceOf(DatePastException::class.java)
    }

    @Test
    fun Member로_MemberResponseDto를_생성할수있다()  {
        val member =
            Member(
                "aaaa",
                "abc@naver.com",
                LocalDate.parse(TestConstant.TESTNOWDATE),
                0,
                BigDecimal(0),
                Level.Bronze,
                1,
            )
        val memberResponseDto = MemberResponseDto.domainToDto(member)

        Assertions.assertThat(memberResponseDto.name).isEqualTo(member.name)
    }
}
// MemberInsertDto의_joinTime의_형식이_yyyyMMdd가_아니면_실패한다 는 컨트롤러테스트해야할듯
