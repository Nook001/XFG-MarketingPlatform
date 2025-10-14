package org.nook.infrastructure.persistent.po

import java.time.LocalDateTime

data class StrategyRule(
    var id: Long? = null,           // 自增ID
    var strategyId: String,         // 抽奖 策略ID
    var awardId: Int?,               // 奖品ID
    var ruleType: String,           // 规则类型
    var ruleModel: String,          // 规则模型
    var ruleValue: String,          // 规则值
    var ruleDesc: String,           // 规则描述
    var createTime: LocalDateTime,  // 创建时间
    var updateTime: LocalDateTime   // 修改时间
)