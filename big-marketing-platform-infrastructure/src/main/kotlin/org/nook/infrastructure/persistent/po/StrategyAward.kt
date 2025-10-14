package org.nook.infrastructure.persistent.po

import java.math.BigDecimal
import java.time.LocalDateTime

data class StrategyAward(
    var id: Long? = null,           // 自增ID
    var strategyId: Long,           // 抽奖 策略ID
    var awardId: Int,               // 奖品ID
    var awardTitle: String,         // 奖品标题
    var awardSubtitle: String?,     // 奖品副标题
    var awardCount: Int,            // 奖品数量
    var awardSurplus: Int,          // 奖品剩余数量
    var awardRate: BigDecimal,      // 奖品概率
    var ruleModels: String,         // 奖品模型
    var sort: Int,                  // 排序
    var createTime: LocalDateTime,  // 创建时间
    var updateTime: LocalDateTime   // 修改时间
)
