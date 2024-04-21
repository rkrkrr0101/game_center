@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.demo.controller

import com.example.demo.common.Result
import com.example.demo.constant.Level
import com.example.demo.customdate.CustomDate
import com.example.demo.member.dto.MemberDeleteDto
import com.example.demo.member.dto.MemberInsertDto
import com.example.demo.member.dto.MemberResponseDto
import com.example.demo.member.dto.MemberUpdateDto
import com.example.demo.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/member")
class MemberControllerImpl(val memberService: MemberService, val customDate: CustomDate) {
    @GetMapping("/all")
    fun findAll(
        @RequestParam("name", required = false) name: String?,
        @RequestParam("level", required = false) level: Level?,
    ): Result<List<MemberResponseDto>> {
        val sort = Sort.by(Sort.Direction.ASC, "joinDate")
        val memberList = memberService.findDynamicSearchSortBy(sort, name, level)
        val responseList = memberList.map { MemberResponseDto.domainToDto(it) }
        return Result(responseList)
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable("id") id: Long,
    ): Result<MemberResponseDto> {
        return Result(MemberResponseDto.domainToDto(memberService.findById(id)))
    }

    @PostMapping("/save")
    fun save(
        @Valid @RequestBody memberInsertDto: MemberInsertDto,
    ) {
        memberInsertDto.customDate = customDate
        memberInsertDto.checkJoinDateYear()
        memberService.save(memberInsertDto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id")id: Long,
    ) {
        val memberDeleteDto = MemberDeleteDto(id)
        memberService.delete(memberDeleteDto)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id")id: Long,
        @Valid @RequestBody memberUpdateDto: MemberUpdateDto,
    ) {
        memberUpdateDto.id = id
        memberUpdateDto.customDate = customDate
        memberUpdateDto.checkJoinDateYear()
        memberService.update(memberUpdateDto)
    }
}
