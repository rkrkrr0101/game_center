package com.example.demo.e2e_test

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
class GameControllerE2ETest @Autowired constructor(val mvc: MockMvc) {
    private fun status() = MockMvcResultMatchers.status()
    private fun jsonPath(key:String) = MockMvcResultMatchers.jsonPath(key)
    @Test
    fun game을_전체조회할수_있다(){
        mvc.perform (MockMvcRequestBuilders.get("/game/all"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.length()").value(3))
    }
}