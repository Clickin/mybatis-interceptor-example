package com.example.mybatisexample.config

import com.example.mybatisexample.mapper.MemberMapper
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@MapperScan(basePackageClasses = [MemberMapper::class])
class MybatisConfig {

    @Bean
    fun sqlSessionFactory(dataSource: DataSource) : SqlSessionFactoryBean = SqlSessionFactoryBean().apply {
        this.setDataSource(dataSource)
        this.setPlugins(SqlLoggerInterceptor())
    }
}