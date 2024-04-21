package com.example.demo.domain.gamecard.repository

import com.example.demo.domain.gamecard.GameCard
import com.example.demo.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface GameCardJpaRepository : JpaRepository<GameCard, Long> {
    @Query("select g from GameCard as g join fetch g.game where g.member=:member")
    fun findByMember(
        @Param("member")member: Member,
    ): List<GameCard>

    fun deleteByMember(member: Member)

    @Query("select g from GameCard as g where g.game.title=:title and g.serialNo=:serialNo")
    fun findByGameAndSerialNo(
        @Param("title")title: String,
        @Param("serialNo")serialNo: Long,
    ): GameCard?
}
