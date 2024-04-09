@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.demo.gamecard

import com.example.demo.baseentity.BaseEntity
import com.example.demo.game.Game
import com.example.demo.member.Member
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
class GameCard(
    var title: String,
    var serialNo: Long,
    var price: BigDecimal,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    var game: Game,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
) : BaseEntity()
