package com.example.demo.member.repository

import com.example.demo.constant.Level
import com.example.demo.member.Member
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<Member, Long> {
    fun findByNameStartsWith(
        name: String,
        sort: Sort,
    ): List<Member>

    fun findByLevel(
        level: Level,
        sort: Sort,
    ): List<Member>

    fun findByNameStartsWithAndLevel(
        name: String,
        level: Level,
        sort: Sort,
    ): List<Member>

    fun findByEmail(email: String): Member?
}
