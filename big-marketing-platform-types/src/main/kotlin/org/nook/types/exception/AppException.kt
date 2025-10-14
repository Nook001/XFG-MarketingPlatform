package org.nook.types.exception

/**
 * 应用异常类
 * 
 * Kotlin 特性：
 * - 继承 RuntimeException
 * - 主构造器 + 次构造器
 * - 不需要 Lombok
 * - 自动生成属性 getter
 */
class AppException : RuntimeException {
    
    /** 异常码 */
    val code: String
    
    /** 异常信息 */
    val info: String?
    
    constructor(code: String) : super() {
        this.code = code
        this.info = null
    }
    
    constructor(code: String, cause: Throwable) : super(cause) {
        this.code = code
        this.info = null
    }
    
    constructor(code: String, message: String) : super(message) {
        this.code = code
        this.info = message
    }
    
    constructor(code: String, message: String, cause: Throwable) : super(message, cause) {
        this.code = code
        this.info = message
    }
    
    override fun toString(): String {
        return "org.nook.x.api.types.exception.XApiException{code='$code', info='$info'}"
    }
    
    companion object {
        private const val serialVersionUID = 5317680961212299217L
    }
}
