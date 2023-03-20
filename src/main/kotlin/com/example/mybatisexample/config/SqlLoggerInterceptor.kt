package com.example.mybatisexample.config


import org.apache.ibatis.cache.CacheKey
import org.apache.ibatis.executor.Executor
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.plugin.*
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.ResultHandler
import org.apache.ibatis.session.RowBounds
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import java.text.DateFormat
import java.util.*


@Intercepts(
    Signature(type = Executor::class, method = "update", args = [MappedStatement::class, Any::class]),
    Signature(
        type = Executor::class,
        method = "query",
        args = [MappedStatement::class, Any::class, RowBounds::class, ResultHandler::class, CacheKey::class, BoundSql::class]
    ),
    Signature(
        type = Executor::class,
        method = "query",
        args = [MappedStatement::class, Any::class, RowBounds::class, ResultHandler::class]
    )
)
/**
 * Mybatis SQL Log 남기기용 Interceptor
 * MappedStatement에서 update, query method를 intercept하여 로그를 남긴다
 */
class SqlLoggerInterceptor : Interceptor {
    private val logger: Logger = LoggerFactory.getLogger(SqlLoggerInterceptor::class.java)

    @Throws(Throwable::class)
    override fun intercept(invocation: Invocation): Any {
        val mappedStatement = invocation.args[0] as MappedStatement
        val parameter: Any? = if (invocation.args.size > 1) {
            invocation.args[1]
        }
        else {
            null
        }
        val sqlId = mappedStatement.id

        val boundSql = mappedStatement.getBoundSql(parameter)
        val configuration = mappedStatement.configuration
        var returnValue: Any? = null
        logger.info("/*---------------Mapper Map ID: $sqlId[begin]---------------*/")
        val sql = geneSql(configuration, boundSql)
        logger.info("==> sql:\n {}\n/*{}*/", sql, sqlId)
        val start = System.currentTimeMillis()
        try {
            returnValue = invocation.proceed()
        }
        catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        val end = System.currentTimeMillis()
        val time = end - start
        logger.info("<== sql END：{} ms", time)
        return returnValue!!
    }

    override fun plugin(target: Any): Any {
        return Plugin.wrap(target, this)
    }

    override fun setProperties(properties: Properties) {
        println(properties)
    }

    companion object {
        private fun getParameterValue(obj: Any?): String {
            val value: String = when (obj) {
                is String -> {
                    "'$obj'"
                }

                is Date -> {
                    val formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.KOREA)
                    "'" + formatter.format(obj) + "'"
                }

                else -> {
                    obj?.toString() ?: ""
                }
            }
            return value
        }

        fun geneSql(configuration: Configuration, boundSql: BoundSql): String {
            // 쿼리실행시 맵핑되는 파라미터를 구한다
            val parameterObject = boundSql.parameterObject
            val parameterMappings = boundSql.parameterMappings
            // 쿼리문을 가져온다(이 상태에서의 쿼리는 값이 들어갈 부분에 ?가 있다)
            var sql = boundSql.sql
            if (parameterMappings.size > 0 && parameterObject != null) {
                val typeHandlerRegistry = configuration.typeHandlerRegistry
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.javaClass)) {
                    sql =
                        sql.replaceFirst("\\?".toRegex(), getParameterValue(parameterObject))
                }
                else {
                    val metaObject = configuration.newMetaObject(parameterObject)
                    for (parameterMapping in parameterMappings) {
                        val propertyName = parameterMapping.property
                        if (metaObject.hasGetter(propertyName)) {
                            val obj = metaObject.getValue(propertyName)
                            sql = sql.replaceFirst("\\?".toRegex(), getParameterValue(obj))
                        }
                        else if (boundSql.hasAdditionalParameter(propertyName)) {
                            val obj = boundSql.getAdditionalParameter(propertyName)
                            sql = sql.replaceFirst("\\?".toRegex(), getParameterValue(obj))
                        }
                    }
                }
            }
            return sql
        }
    }
}