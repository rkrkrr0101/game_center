package com.example.demo.mock

import com.example.demo.constant.Level
import com.example.demo.member.Member
import com.example.demo.member.repository.MemberRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Sort
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import java.util.*
import java.util.concurrent.atomic.AtomicLong


//소팅이 하드코딩으로 joinDate로 박혀서 다른거로 소팅이 안되니 주의
//나중에 수정하자..
class MemberRepositoryFake:MemberRepository {
    private var generatedId:AtomicLong= AtomicLong(0)
    private val memberList:MutableList<Member> = Collections.synchronizedList(mutableListOf<Member>())
    override fun save(member: Member): Member {
        if (member.id==null || member.id==0L){
            val newMember=Member(
                member.name,
                member.email,
                member.joinDate,
                member.totalCardQuantity,
                member.totalCardPrice,
                member.level,
                generatedId.incrementAndGet()
            )
            memberList.add(newMember)
            return newMember
        }else{
            memberList.removeIf { it.id==member.id }
            memberList.add(member)
            return member
        }
    }

    override fun delete(member: Member) {
        val removeIf = memberList.removeIf { it.id == member.id }
        if (removeIf==false){
            throw JpaObjectRetrievalFailureException(EntityNotFoundException("delete예외발생"))
        }
    }

    override fun findById(id: Long): Member {
        return memberList.find { it.id==id }
            ?:throw JpaObjectRetrievalFailureException(EntityNotFoundException("findById예외발생"))
    }

    override fun findAll(sort: Sort): List<Member> {


        return memberList.sortedBy { it.joinDate }//sort받아서 돌려야하는데 못찾겠음

    }

    override fun findByEmail(email: String): Member? {
        return memberList.find { it.email==email }
    }

    override fun findByNameStartsWith(name: String, sort: Sort): List<Member> {
        return memberList.asSequence()
            .filter { it.name.startsWith(name) }
            .sortedBy { it.joinDate }
            .toList()
    }

    override fun findByLevel(level: Level, sort: Sort): List<Member> {
        return memberList
            .asSequence()
            .filter { it.level==level }
            .sortedBy { it.joinDate }
            .toList()
    }

    override fun findByNameStartsWithAndLevel(name: String, level: Level, sort: Sort): List<Member> {
        return memberList
            .asSequence()
            .filter { it.name.startsWith(name) }
            .filter { it.level==level }
            .sortedBy { it.joinDate }
            .toList()
    }
}