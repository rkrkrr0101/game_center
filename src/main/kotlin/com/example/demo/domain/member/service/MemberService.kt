package com.example.demo.domain.member.service

import com.example.demo.alert.AlertPort
import com.example.demo.constant.Level
import com.example.demo.domain.gamecard.repository.GameCardRepository
import com.example.demo.domain.member.Member
import com.example.demo.domain.member.dto.MemberDeleteDto
import com.example.demo.domain.member.dto.MemberInsertDto
import com.example.demo.domain.member.dto.MemberUpdateDto
import com.example.demo.domain.member.repository.MemberRepository
import com.example.demo.util.LogUtil
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    val memberRepository: MemberRepository,
    val gameCardRepository: GameCardRepository,
    val alertPortList: List<AlertPort>,
) {
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
            LogUtil.emailDuplicateThrow("save", memberInsertDto.email)
        }
        val saveMember = memberRepository.save(memberInsertDto.dtoToDomain())
        for (alertPort in alertPortList) {
            alertPort.send(saveMember)
        }
    }

    @Transactional
    fun update(memberUpdateDto: MemberUpdateDto) {
        if (!isUniqueEmail(memberUpdateDto.email, memberUpdateDto.id)) {
            LogUtil.emailDuplicateThrow("update", memberUpdateDto.email)
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
        updaterId: Long,
    ): Boolean { // update시 사용
        val member = memberRepository.findByEmail(email) ?: return true
        if (isEqualMember(member, updaterId)) return true
        return false
    }

    private fun isEqualMember(
        member: Member,
        updaterId: Long,
    ): Boolean {
        return member.id == updaterId
    }
}
