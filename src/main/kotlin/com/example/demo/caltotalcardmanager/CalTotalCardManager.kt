package com.example.demo.caltotalcardmanager

import com.example.demo.constant.Level
import com.example.demo.gamecard.GameCard
import java.math.BigDecimal

class CalTotalCardManager {
    fun calTotalCardQuantityAndPrice(cardList: List<GameCard>): Pair<Int, BigDecimal> {
        var resPrice = BigDecimal("0")
        var resQuantity = 0
        for (i in cardList) {
            resPrice += i.price
            resQuantity += 1
        }
        return Pair(resQuantity, resPrice)
    }

    fun calLevel(cardList: List<GameCard>): Level {
        val gameSet = hashSetOf<String>()
        var gameCardQuantity = 0
        var gameCardPrice = BigDecimal("0")
        for (i in cardList) {
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
