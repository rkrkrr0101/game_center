@file:Suppress("ktlint:standard:function-naming")

package com.example.demo.e2e

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ViewControllerE2ETest
    @Autowired
    constructor(
        val mvc: MockMvc,
    ) {
        private fun status() = MockMvcResultMatchers.status()

        private fun view() = MockMvcResultMatchers.view()

        private fun model() = MockMvcResultMatchers.model()

        @Test
        fun memberview를_조회할수_있다() {
            mvc.perform(MockMvcRequestBuilders.get("/view/memberview"))
                .andExpect(status().isOk)
                .andExpect(view().name("memberView"))
        }

        @Test
        fun findmember를_조회할수_있다() {
            mvc.perform(MockMvcRequestBuilders.get("/view/findmember?id=1"))
                .andExpect(status().isOk)
                .andExpect(view().name("findMember"))
                .andExpect(model().attribute("id", (1).toLong()))
        }

        @Test
        fun savemember를_조회할수_있다() {
            mvc.perform(MockMvcRequestBuilders.get("/view/savemember"))
                .andExpect(status().isOk)
                .andExpect(view().name("saveMember"))
        }

        @Test
        fun updatemember를_조회할수_있다() {
            mvc.perform(MockMvcRequestBuilders.get("/view/updatemember?id=1"))
                .andExpect(status().isOk)
                .andExpect(view().name("updateMember"))
                .andExpect(model().attribute("id", (1).toLong()))
        }

        @Test
        fun savegamecard를_조회할수_있다() {
            mvc.perform(MockMvcRequestBuilders.get("/view/savegamecard?id=1"))
                .andExpect(status().isOk)
                .andExpect(view().name("saveGameCard"))
                .andExpect(model().attribute("id", (1).toLong()))
        }
    }
