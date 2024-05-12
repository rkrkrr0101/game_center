@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.demo.domain.gamecard

import com.example.demo.domain.BaseEntity
import com.example.demo.domain.game.Game
import com.example.demo.domain.member.Member
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
class GameCard(
    title: String,
    serialNo: Long,
    price: BigDecimal,
    game: Game,
    member: Member,
    id: Long = 0,
) : BaseEntity() {
    var title = title
        protected set
    var serialNo = serialNo
        protected set
    var price = price
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    var game = game
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member = member
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = id
        protected set
}
