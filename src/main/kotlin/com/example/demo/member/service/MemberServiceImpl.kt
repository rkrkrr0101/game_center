package com.example.demo.member.service

import com.example.demo.alert.AlertPort
import com.example.demo.constant.Level
import com.example.demo.exception.exception.EmailDuplicateException
import com.example.demo.gamecard.repository.GameCardRepository
import com.example.demo.member.Member
import com.example.demo.member.dto.MemberDeleteDto
import com.example.demo.member.dto.MemberInsertDto
import com.example.demo.member.dto.MemberUpdateDto
import com.example.demo.member.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    val memberRepository: MemberRepository,
    val gameCardRepository: GameCardRepository,
    val alertPort: AlertPort,
) : MemberService {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun findDynamicSearchSortBy(
        sort: Sort,
        name: String?,
        level: Level?,
    ): List<Member> {
        if (name == null && level == null) {
            return memberRepository.findAll(sort)
        }
        if (name == null) {
            return memberRepository.findByLevel(level!!, sort)
        }
        if (level == null) {
            return memberRepository.findByNameStartsWith(name, sort)
        }
        return memberRepository.findByNameStartsWithAndLevel(name, level, sort)
    }

    override fun findById(id: Long): Member {
        return memberRepository.findById(id)
    }

    @Transactional
    override fun save(memberInsertDto: MemberInsertDto) {
        if (!isUniqueEmail(memberInsertDto.email)) {
            log.warn("멤버의 추가중 이메일중복:{}", memberInsertDto.email)
            throw EmailDuplicateException("이메일이 중복입니다", memberInsertDto.email)
        }
        val saveMember = memberRepository.save(memberInsertDto.dtoToDomain())
        alertPort.send(saveMember)
    }

    @Transactional
    override fun update(memberUpdateDto: MemberUpdateDto) {
        if (!isUniqueEmail(memberUpdateDto.email, memberUpdateDto.id)) {
            log.warn("{}의 수정시 이메일중복:{}", memberUpdateDto.id, memberUpdateDto.email)
            throw EmailDuplicateException("이메일이 중복입니다", memberUpdateDto.email)
        }
        val findMember = memberRepository.findById(memberUpdateDto.id)
        findMember.name = memberUpdateDto.name
        findMember.email = memberUpdateDto.email
        findMember.joinDate = memberUpdateDto.joinDate
    }

    @Transactional
    override fun delete(memberDeleteDto: MemberDeleteDto) {
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
