package com.example.demo.domain.member.service

import com.example.demo.alert.AlertPort
import com.example.demo.constant.Level
import com.example.demo.domain.gamecard.repository.GameCardRepository
import com.example.demo.domain.member.Member
import com.example.demo.domain.member.dto.MemberDeleteDto
import com.example.demo.domain.member.dto.MemberInsertDto
import com.example.demo.domain.member.dto.MemberUpdateDto
import com.example.demo.domain.member.repository.MemberRepository
import com.example.demo.exception.exception.EmailDuplicateException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    val memberRepository: MemberRepository,
    val gameCardRepository: GameCardRepository,
    val alertPort: AlertPort,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun findDynamicSearchSortBy(
        sort: Sort,
        name: String? = null,
        level: Level? = null,
    ): List<Member> {
        return memberRepository.findByDynamic(sort, name, level)
    }

    fun findById(id: Long): Member {
        return memberRepository.findById(id)
    }

    @Transactional
    fun save(memberInsertDto: MemberInsertDto) {
        if (!isUniqueEmail(memberInsertDto.email)) {
            log.warn("멤버의 추가중 이메일중복:{}", memberInsertDto.email)
            throw EmailDuplicateException("이메일이 중복입니다", memberInsertDto.email)
        }
        val saveMember = memberRepository.save(memberInsertDto.dtoToDomain())
        alertPort.send(saveMember)
    }

    @Transactional
    fun update(memberUpdateDto: MemberUpdateDto) {
        if (!isUniqueEmail(memberUpdateDto.email, memberUpdateDto.id)) {
            log.warn("{}의 수정시 이메일중복:{}", memberUpdateDto.id, memberUpdateDto.email)
            throw EmailDuplicateException("이메일이 중복입니다", memberUpdateDto.email)
        }
        val findMember = memberRepository.findById(memberUpdateDto.id)
        findMember.update(
            name = memberUpdateDto.name,
            email = memberUpdateDto.email,
            joinDate = memberUpdateDto.joinDate,
        )
    }

    @Transactional
    fun delete(memberDeleteDto: MemberDeleteDto) {
        val findMember = memberRepository.findById(memberDeleteDto.id)

        gameCardRepository.deleteByMember(findMember)
        memberRepository.delete(findMember)
    }

    private fun isUniqueEmail(email: String): Boolean {
        memberRepository.findByEmail(email) ?: return true
        return false
    }

    private fun isUniqueEmail(
        email: String,
        memberId: Long,
    ): Boolean { // update시 사용
        val member = memberRepository.findByEmail(email) ?: return true
        if (member.id == memberId) { // 자기자신의 이메일일경우 유니크로 취급
            return true
        }
        return false
    }
}
