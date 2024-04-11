package com.example.demo.member.repository

import com.example.demo.constant.Level
import com.example.demo.member.Member
import org.springframework.data.domain.Sort

interface MemberRepository {
    fun save(member: Member): Member

    fun delete(member: Member)

    fun findById(id: Long): Member

    fun findAll(sort: Sort): List<Member>

    fun findByEmail(email: String): Member?

    fun findByDynamic(
        sort: Sort,
        name: String?,
        level: Level?,
    ): List<Member>
}
