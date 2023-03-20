package com.example.mybatisexample.mapper

import com.example.mybatisexample.mapper.spec.FindMemberByNameSpec
import org.apache.ibatis.jdbc.SQL


class MemberMapperSQL {
    fun findMembersByName(findMemberByNameSpec: FindMemberByNameSpec) =
        // Java DSL to Kotlin DSL
        object : SQL() {
        init {
            SELECT("id", "member_name")
            FROM("SCOTT.MEMBER")
            if (findMemberByNameSpec.memberName?.isNotBlank() == true) {
                WHERE("member_name like #{memberName} || '%'")
            }
            ORDER_BY("id")
        }
    }.toString()
}