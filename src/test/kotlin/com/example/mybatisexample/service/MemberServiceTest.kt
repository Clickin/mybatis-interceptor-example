package com.example.mybatisexample.service

import com.example.mybatisexample.dto.Member
import com.example.mybatisexample.mapper.spec.FindMemberByNameSpec
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class MemberServiceTest(
    @Qualifier("memberService")
    private val memberService: MemberService
) {

    @Test
    fun findMembersByName() {
        val memberList = runBlocking { memberService.findMembersByName(FindMemberByNameSpec("tiger")) }
        val expectedList = listOf(Member(id = 1, memberName = "tiger"), Member(id = 3, memberName = "tiger king"))
        Assertions.assertArrayEquals(memberList.toTypedArray(), expectedList.toTypedArray())



    }
}