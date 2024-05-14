package com.example.demo.util

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class QueryDslUtil {
    companion object {
        fun <T : EntityPathBase<*>> sortToOrderSpecifier(
            sort: Sort,
            type: T,
        ): Array<OrderSpecifier<*>> {
            val resList = mutableListOf<OrderSpecifier<*>>()

            val pathBuilder = PathBuilder(type.type, type.metadata)

            for (i in sort) {
                val orderSpecifier =
                    OrderSpecifier(if (i.isAscending) Order.ASC else Order.DESC, pathBuilder.getString(i.property))
                resList.add(orderSpecifier)
            }
            return resList.toTypedArray()
        }

        fun <T : EntityPathBase<*>> pageableToOrderSpecifier(
            pageable: Pageable,
            type: T,
        ): Array<OrderSpecifier<*>> {
            val resList = mutableListOf<OrderSpecifier<*>>()

            val pathBuilder = PathBuilder(type.type, type.metadata)

            for (i in pageable.sort) {
                val orderSpecifier =
                    OrderSpecifier(if (i.isAscending) Order.ASC else Order.DESC, pathBuilder.getString(i.property))
                resList.add(orderSpecifier)
            }
            return resList.toTypedArray()
        }
    }
}
