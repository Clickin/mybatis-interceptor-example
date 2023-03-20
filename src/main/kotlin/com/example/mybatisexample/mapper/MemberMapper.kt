package com.example.mybatisexample.mapper

import com.example.mybatisexample.dto.Member
import com.example.mybatisexample.mapper.spec.FindMemberByNameSpec
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.SelectProvider

@Mapper
interface MemberMapper {
    // Java SQL builder
    @SelectProvider(MemberMapperSQL::class, method = "findMembersByName")
    fun findMembersByName(findMemberByNameSpec: FindMemberByNameSpec) : List<Member>
}