package org.nook.infrastructure.persistence.po

import java.time.LocalDateTime

data class StrategyPO (
    var id: Long? = null,           // 自增ID
    var strategyId: Long,           // 抽奖 策略ID
    var strategyDesc: String,       // 抽奖 策略描述
    var ruleModels: String?,        // 规则模型
    var createTime: LocalDateTime,  // 创建时间
    var updateTime: LocalDateTime   // 修改时间
)

