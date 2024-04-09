package com.example.demo.member.repository

import com.example.demo.constant.Level
import com.example.demo.member.Member
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrElse

@Repository
class MemberRepositoryImpl(val memberJpaRepository: MemberJpaRepository) : MemberRepository {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun save(member: Member): Member {
        return memberJpaRepository.save(member)
    }

    override fun delete(member: Member) {
        memberJpaRepository.delete(member)
    }

    override fun findById(id: Long): Member {
        return memberJpaRepository.findById(id).getOrElse {
            log.warn("id={}의 멤버를 찾을수 없습니다", id)
            throw EntityNotFoundException("""member의 객체를 찾을수없음 id=$id""")
        }
    }

    override fun findByEmail(email: String): Member? {
        return memberJpaRepository.findByEmail(email)
    }

    override fun findAll(sort: Sort): List<Member> {
        return memberJpaRepository.findAll(sort)
    }

    override fun findByNameStartsWith(
        name: String,
        sort: Sort,
    ): List<Member> {
        return memberJpaRepository.findByNameStartsWith(name, sort)
    }

    override fun findByLevel(
        level: Level,
        sort: Sort,
    ): List<Member> {
        return memberJpaRepository.findByLevel(level, sort)
    }

    override fun findByNameStartsWithAndLevel(
        name: String,
        level: Level,
        sort: Sort,
    ): List<Member> {
        return memberJpaRepository.findByNameStartsWithAndLevel(name, level, sort)
    }
}
