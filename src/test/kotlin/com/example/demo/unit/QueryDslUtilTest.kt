@file:Suppress("ktlint:standard:function-naming")

package com.example.demo.unit

import com.example.demo.common.QueryDslUtil
import com.example.demo.domain.member.QMember
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class QueryDslUtilTest {
    @Test
    fun Sort객체를_OrderSpecifier배열객체로_변환할수_있다() {
        var mainSort = Sort.by(Sort.Direction.ASC, "joinDate")
        val sort1 = Sort.by(Sort.Direction.ASC, "name")
        val sort2 = Sort.by(Sort.Direction.ASC, "age")
        mainSort = mainSort.and(sort1)
        mainSort = mainSort.and(sort2)

        val orderSpecifierArray = QueryDslUtil.sortToOrderSpecifier(mainSort, QMember.member)

        Assertions.assertThat(orderSpecifierArray.size).isEqualTo(3)
        Assertions.assertThat(orderSpecifierArray[0].target.toString()).contains("joinDate")
        Assertions.assertThat(orderSpecifierArray[1].target.toString()).contains("name")
        Assertions.assertThat(orderSpecifierArray[2].target.toString()).contains("age")
    }

    @Test
    fun Pageable객체를_OrderSpecifier배열객체로_변환할수_있다() {
        var mainSort = Sort.by(Sort.Direction.ASC, "joinDate")
        val sort1 = Sort.by(Sort.Direction.ASC, "name")
        val sort2 = Sort.by(Sort.Direction.ASC, "age")
        mainSort = mainSort.and(sort1)
        mainSort = mainSort.and(sort2)
        val pageable = PageRequest.of(1, 5, mainSort)

        val orderSpecifierList = QueryDslUtil.pageableToOrderSpecifier(pageable, QMember.member)

        Assertions.assertThat(orderSpecifierList.size).isEqualTo(3)
        Assertions.assertThat(orderSpecifierList[0].target.toString()).contains("joinDate")
        Assertions.assertThat(orderSpecifierList[1].target.toString()).contains("name")
        Assertions.assertThat(orderSpecifierList[2].target.toString()).contains("age")
    }
}
