@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.demo.domain.gamecard.dto

import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.math.RoundingMode

data class GameCardInsertDto(
    @field:Size(max = 100, message = "게임카드의 이름길이를 확인해주세요")
    @field:NotBlank(message = "게임카드의 이름이 공백이면 안됩니다")
    val title: String,
    @field:Min(value = 1, message = "일련번호는 1보다 커야합니다")
    val serialNo: Long,
    @field:DecimalMin(value = "0", message = "가격은 0보다 커야합니다")
    @field:DecimalMax(value = "100000", message = "가격은 100000보다 작아야합니다")
    var price: BigDecimal,
    val gameId: Long,
    val memberId: Long,
) {
    init {
        price = price.setScale(2, RoundingMode.HALF_UP)
    }
}
