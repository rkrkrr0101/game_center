package com.example.demo.domain.member.repository

import com.example.demo.constant.Level
import com.example.demo.domain.member.Member
import com.example.demo.domain.member.QMember
import com.example.demo.util.LogUtil
import com.example.demo.util.QueryDslUtil
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import kotlin.jvm.optionals.getOrElse

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository,
    private val qf: JPAQueryFactory,
) : MemberRepository {
    override fun save(member: Member): Member {
        return memberJpaRepository.save(member)
    }

    override fun delete(member: Member) {
        memberJpaRepository.delete(member)
    }

    override fun findById(id: Long): Member {
        return memberJpaRepository.findById(id).getOrElse {
            LogUtil.emptyFindThrow("member", id)
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
