package com.example.demo.Integration_test.repository

import com.example.demo.game.repository.GameRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class GameRepositoryTest @Autowired constructor(val gameRepository: GameRepository){
    //game은 data.sql의 자동 init에 의존해서,예외가뜨면 그쪽 뭐 건드렸는지 확인
    //로직이 이렇게가는게 그대로면 그냥 빼버려도 되긴할듯
    @Test
    fun game을_id로_조회할수_있다(){
        val gameList = gameRepository.findAll()
        val findGame = gameRepository.findById(gameList[0].id)

        Assertions.assertThat(gameList[0].id).isEqualTo(findGame.id)
    }
    @Test
    fun game의_Id가_없다면_예외가_발생한다(){
        Assertions.assertThatThrownBy { gameRepository.findById(100)  }
            .isInstanceOf(JpaObjectRetrievalFailureException::class.java)
    }
    @Test
    fun game을_전체조회할수_있다(){
        val gameList = gameRepository.findAll()
        Assertions.assertThat(gameList.size).isEqualTo(3)
    }
}