package org.nook.domain.strategy.model.raffle

import org.nook.domain.strategy.model.enums.RaffleStage
import org.nook.domain.strategy.model.enums.RuleLogicCheckTypeVO


// 规则过滤后的行为
class RuleActionEntity(
    val code: String = RuleLogicCheckTypeVO.ALLOW.code,
    val info: String = RuleLogicCheckTypeVO.ALLOW.info,
    val ruleModel: String? = null,
    val data: RaffleResultEntity? = null,
)

// 储存抽奖流程上下文
open class RaffleResultEntity(
    val strategyId: Long,
    val stage: RaffleStage,
    val ruleWeightValueKey: String? = null,
    val awardId: Int? = null,
)


