package com.example.demo.member.service

import com.example.demo.constant.Level
import com.example.demo.member.Member
import com.example.demo.member.dto.MemberDeleteDto
import com.example.demo.member.dto.MemberInsertDto
import com.example.demo.member.dto.MemberUpdateDto
import org.springframework.data.domain.Sort

interface MemberService {
    fun findDynamicSearchSortBy(
        sort: Sort,
        name: String? = null,
        level: Level? = null,
    ): List<Member>

    fun findById(id: Long): Member

    fun save(memberInsertDto: MemberInsertDto)

    fun update(memberUpdateDto: MemberUpdateDto)

    fun delete(memberDeleteDto: MemberDeleteDto)
}
