package com.example.mybatisexample.service

import com.example.mybatisexample.dto.Member
import com.example.mybatisexample.mapper.MemberMapper
import com.example.mybatisexample.mapper.spec.FindMemberByNameSpec
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberMapper: MemberMapper
) {

    suspend fun findMembersByName(findMemberByNameSpec: FindMemberByNameSpec) : List<Member> {
        return memberMapper.findMembersByName(findMemberByNameSpec)
    }
}