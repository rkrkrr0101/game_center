@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.demo.member

import com.example.demo.baseentity.BaseEntity
import com.example.demo.constant.Level
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
    @Id
    @GeneratedValue
    var id: Long = 0,
) : BaseEntity()
