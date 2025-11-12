package org.nook.infrastructure.repository

import org.nook.domain.strategy.model.entity.StrategyAwardEntity
import org.nook.domain.strategy.model.entity.StrategyEntity
import org.nook.domain.strategy.model.entity.StrategyRuleEntity
import org.nook.infrastructure.persistence.po.StrategyAwardPO
import org.nook.infrastructure.persistence.po.StrategyPO
import org.nook.infrastructure.persistence.po.StrategyRulePO


fun StrategyAwardPO.toEntity() = StrategyAwardEntity(
    strategyId = this.strategyId,
    awardId = this.awardId,
    awardCount = this.awardCount,
    awardCountSurplus = this.awardSurplus,
    awardRate = this.awardRate
)

fun StrategyPO.toEntity() = StrategyEntity(
    strategyId = this.strategyId,
    strategyDesc = this.strategyDesc,
    ruleModels = this.ruleModels
)

fun StrategyRulePO.toEntity() = StrategyRuleEntity(
    strategyId = this.strategyId,
    awardId = this.awardId,
    ruleType = this.ruleType,
    ruleModel = this.ruleModel,
    ruleValue = this.ruleValue,
    ruleDesc = this.ruleDesc
)