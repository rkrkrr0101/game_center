@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.demo.member

import com.example.demo.baseentity.BaseEntity
import com.example.demo.constant.Level
import com.example.demo.gamecard.GameCard
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
class Member(
    var name: String,
    var email: String,
    var joinDate: LocalDate,
    var totalCardQuantity: Int = 0,
    var totalCardPrice: BigDecimal = BigDecimal("0"),
    @Enumerated(EnumType.STRING)
    var level: Level = Level.Bronze,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var gameCardList: MutableList<GameCard> = mutableListOf(),
    @Id
    @GeneratedValue
    var id: Long = 0,
) : BaseEntity() {
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
