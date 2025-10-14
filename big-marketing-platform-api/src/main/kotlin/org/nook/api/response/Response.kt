package org.nook.api.response

import java.io.Serializable

/**
 * 通用响应类
 * 
 * Kotlin 特性：
 * - data class 自动生成 equals, hashCode, toString, copy
 * - 不需要 Lombok 注解
 * - 泛型支持
 * - 默认参数值
 */
data class Response<T>(
    val code: String? = null,
    val info: String? = null,
    val data: T? = null
) : Serializable {
    companion object {
        private const val serialVersionUID = 7000723935764546321L
    }
}
