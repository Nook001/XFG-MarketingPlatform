package org.nook.types.enums

/**
 * 响应状态码枚举
 * 
 * Kotlin 特性：
 * - enum class 定义枚举
 * - 主构造器直接定义属性
 * - 不需要 Lombok（自动生成 getter）
 */
enum class ResponseCode(
    val code: String,
    val info: String
) {
    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数");
}
