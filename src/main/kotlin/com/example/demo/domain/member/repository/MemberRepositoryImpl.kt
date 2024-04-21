package com.example.demo.domain.member.repository

import com.example.demo.common.QueryDslUtil
import com.example.demo.constant.Level
import com.example.demo.domain.member.Member
import com.example.demo.member.QMember
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import kotlin.jvm.optionals.getOrElse

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository,
    private val qf: JPAQueryFactory,
) : MemberRepository {
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

    override fun findByDynamic(
        sort: Sort,
        name: String?,
        level: Level?,
    ): List<Member> {
        val member = QMember.member
        return qf.selectFrom(member)
            .where(nameLike(name), levelEq(level))
            .orderBy(*QueryDslUtil.sortToOrderSpecifier(sort, member))
            .fetch()
    }

    private fun nameLike(name: String?): BooleanExpression? {
        if (StringUtils.hasText(name)) {
            return QMember.member.name.like("""$name%""")
        }
        return null
    }

    private fun levelEq(level: Level?): BooleanExpression? {
        if (level != null) {
            return QMember.member.level.eq(level)
        }
        return null
    }
}
