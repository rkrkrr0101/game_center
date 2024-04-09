package com.example.demo.Integration_test.repository

import com.example.demo.constant.Level
import com.example.demo.member.Member
import com.example.demo.member.repository.MemberRepository
import com.example.demo.mock.TestConstant
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
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
class MemberRepositoryTest @Autowired constructor( val memberRepository: MemberRepository){

    @Test
    fun member를_저장할수_있다(){
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))

        val findMember = memberRepository.save(member)

        Assertions.assertThat(member).isEqualTo(findMember)
    }
    @Test
    fun member를_삭제할수_있다(){
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        val findMember = memberRepository.save(member)
        val sort = Sort.by(Sort.Direction.DESC, "joinDate")


        memberRepository.delete(findMember)

        val memberList = memberRepository.findAll(sort)

        Assertions.assertThat(memberList.size).isEqualTo(0)
    }
    @Test
    fun member를_찾을수_있다(){
        val member = Member("aaaa", "aa@naver.com", LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val findMember = memberRepository.findById(member.id)

        Assertions.assertThat(findMember.id).isEqualTo(member.id)
    }
    @Test
    fun member의_Id가_없다면_예외가_발생한다(){
        Assertions.assertThatThrownBy { memberRepository.findById(100)  }
            .isInstanceOf(JpaObjectRetrievalFailureException::class.java)
    }
    @Test
    fun member의_이메일로_멤버를_찾을수_있다(){
        val email="aa@naver.com"
        val member = Member("aaaa", email, LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member)

        val findMember = memberRepository.findByEmail(email)

        Assertions.assertThat(findMember!!.id).isEqualTo(member.id)
    }
    @Test
    fun 이메일검색시_해당하는_멤버가_없으면_null을_반환한다(){
        val email="aa@naver.com"

        val findMember = memberRepository.findByEmail(email)

        Assertions.assertThat(findMember).isNull()
    }
    @Test
    fun 정렬해서_멤버목록을_전체조회할수_있다(){
        val member1 = Member("aaaa", "aa@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member1)
        val member2 = Member("aaaa", "bb@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(1))
        memberRepository.save(member2)
        val member3 = Member("aaaa", "cc@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(2))
        memberRepository.save(member3)
        val sort = Sort.by(Sort.Direction.ASC, "joinDate")

        val memberList = memberRepository.findAll(sort)

        Assertions.assertThat(memberList[0].id).isEqualTo(member3.id)

    }
    @Test
    fun 정렬해서_이름의_왼쪽_like로_조회할수_있다(){
        val member1 = Member("aaaa", "aa@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE))
        memberRepository.save(member1)
        val member2 = Member("abcd", "bb@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(1))
        memberRepository.save(member2)
        val member3 = Member("aabc", "cc@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(2))
        memberRepository.save(member3)
        val sort = Sort.by(Sort.Direction.ASC, "joinDate")

        val memberList = memberRepository.findByNameStartsWith("aa", sort)

        Assertions.assertThat(memberList.size).isEqualTo(2)
        Assertions.assertThat(memberList[0].id).isEqualTo(member3.id)
        Assertions.assertThat(memberList[1].id).isEqualTo(member1.id)
    }
    @Test
    fun 정렬해서_레벨로_조회할수있다(){
        val member1 = Member("aaaa", "aa@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE),
            5,
            BigDecimal(300),
            Level.Gold,
        )
        memberRepository.save(member1)
        val member2 = Member("abcd", "bb@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(1),
            1,
            BigDecimal(1),
            Level.Silver,
        )
        memberRepository.save(member2)
        val member3 = Member("aabc", "cc@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(2),
            0,
            BigDecimal(0),
            Level.Bronze
        )
        memberRepository.save(member3)
        val sort = Sort.by(Sort.Direction.ASC, "joinDate")

        val memberList = memberRepository.findByLevel(Level.Gold, sort)

        Assertions.assertThat(memberList.size).isEqualTo(1)
        Assertions.assertThat(memberList[0].id).isEqualTo(member1.id)
    }
    @Test
    fun 정렬해서_이름과_레벨로_조회할수있다(){
        val member1 = Member("aaaa", "aa@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE),
            5,
            BigDecimal(300),
            Level.Gold,
        )
        memberRepository.save(member1)
        val member2 = Member("abcd", "bb@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(1),
            1,
            BigDecimal(1),
            Level.Silver,
        )
        memberRepository.save(member2)
        val member3 = Member("aabc", "cc@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(2),
            0,
            BigDecimal(0),
            Level.Bronze
        )
        memberRepository.save(member3)
        val member4 = Member("wwww", "aa@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE),
            5,
            BigDecimal(300),
            Level.Gold,
        )
        memberRepository.save(member4)
        val member5 = Member("aazx", "aa@naver.com",
            LocalDate.parse(TestConstant.TESTNOWDATE).minusDays(5),
            5,
            BigDecimal(300),
            Level.Gold,
        )
        memberRepository.save(member5)
        val sort = Sort.by(Sort.Direction.ASC, "joinDate")


        val memberList = memberRepository.findByNameStartsWithAndLevel("aa",Level.Gold, sort)

        Assertions.assertThat(memberList.size).isEqualTo(2)
        Assertions.assertThat(memberList[0].id).isEqualTo(member5.id)
        Assertions.assertThat(memberList[1].id).isEqualTo(member1.id)
    }

}