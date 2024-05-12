@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.demo.domain.member

import com.example.demo.constant.Level
import com.example.demo.domain.BaseEntity
import com.example.demo.domain.gamecard.GameCard
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
class Member(
    name: String,
    email: String,
    joinDate: LocalDate,
    totalCardQuantity: Int = 0,
    totalCardPrice: BigDecimal = BigDecimal("0"),
    level: Level = Level.Bronze,
    gameCardList: MutableList<GameCard> = mutableListOf(),
    id: Long = 0,
) : BaseEntity() {
    var name = name
        protected set
    var email = email
        protected set
    var joinDate = joinDate
        protected set
    var totalCardQuantity = totalCardQuantity
        protected set
    var totalCardPrice = totalCardPrice
        protected set

    @Enumerated(EnumType.STRING)
    var level = level
        protected set

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var gameCardList = gameCardList
        protected set

    @Id
    @GeneratedValue
    var id = id
        protected set

    fun update(
        name: String? = null,
        email: String? = null,
        joinDate: LocalDate? = null,
    ) {
        this.name = name ?: this.name
        this.email = email ?: this.email
        this.joinDate = joinDate ?: this.joinDate
    }

    fun addGameCard(gameCard: GameCard) {
        gameCardList.add(gameCard)
        totalCardPrice += gameCard.price
        totalCardQuantity += 1
        level = calLevel()
    }

    fun removeGameCard(gameCard: GameCard) {
        gameCardList.remove(gameCard)
        totalCardPrice -= gameCard.price
        totalCardQuantity -= 1
        level = calLevel()
    }

    private fun calLevel(): Level {
        val gameSet = hashSetOf<String>()
        var gameCardQuantity = 0
        var gameCardPrice = BigDecimal("0")
        for (i in gameCardList) {
            if (i.price <= BigDecimal("0")) {
                continue
            }
            gameSet.add(i.game.title)
            gameCardQuantity += 1
            gameCardPrice += i.price
        }
        return levelSelect(gameCardQuantity, gameCardPrice, gameSet)
    }

    private fun levelSelect(
        gameCardQuantity: Int,
        gameCardPrice: BigDecimal,
        gameSet: HashSet<String>,
    ): Level {
        if (gameCardQuantity >= 4 || (gameCardPrice > BigDecimal("100") && gameCardQuantity >= 2)) {
            if (gameSet.size >= 2) {
                return Level.Gold
            }
        }
        if (gameCardQuantity >= 1) {
            return Level.Silver
        }
        return Level.Bronze
    }
}
