package org.nook.domain.strategy.service.raffle

import org.nook.domain.strategy.model.raffle.RaffleFilterEntity
import org.nook.domain.strategy.model.raffle.RaffleInputEntity
import org.nook.domain.strategy.model.raffle.RaffleResultEntity
import org.nook.domain.strategy.model.raffle.RuleActionEntity
import org.nook.domain.strategy.model.enums.RuleLogicCheckTypeVO
import org.nook.domain.strategy.repository.IRepository
import org.nook.domain.strategy.service.dispatcher.IStrategyDispatcher
import org.nook.domain.strategy.service.rule.IRuleFilter
import org.nook.domain.strategy.service.rule.RaffleFilterFactory
import org.nook.domain.strategy.service.rule.filter.RuleWeightFilter
import org.nook.types.exception.AppException
import org.springframework.stereotype.Service

@Service
class RaffleStrategy(
    repository: IRepository,
    dispatcher: IStrategyDispatcher,
    val raffleFilterFactory: RaffleFilterFactory,
    private val ruleWeightFilter: RuleWeightFilter
): AbstractRaffleStrategy( repository, dispatcher){

    override fun doCheckRaffleBeforeFilter(raffleInputEntity: RaffleInputEntity, logics: List<String>): RuleActionEntity {
        // 获取 [规则代码，过滤器实例]map
        val ruleFilters: Map<String, IRuleFilter<RaffleResultEntity>> = raffleFilterFactory.logicFilterMap

        // 优先尝试 过滤黑名单规则
        logics.firstOrNull { logic -> logic.contains(RaffleFilterFactory.LogicModel.RULE_BLACKLIST.code) }?.let{
            // 如果存在黑名单规则
            // 获取过滤器实例
            val ruleFilter: IRuleFilter<RaffleResultEntity> = ruleFilters[RaffleFilterFactory.LogicModel.RULE_BLACKLIST.code]!!

            // 创建过滤器上下文
            val raffleFilterEntity = RaffleFilterEntity(
                userId = raffleInputEntity.userId,
                awardId = 0,
                strategyId = raffleInputEntity.strategyId,
                ruleModel = RaffleFilterFactory.LogicModel.RULE_BLACKLIST.code,
            )

            // 获取过滤结果
            val raffleResultEntity = ruleFilter.filter(raffleFilterEntity)

            // 如果被规则拦截
            if (raffleResultEntity.code != RuleLogicCheckTypeVO.ALLOW.code) {
                return raffleResultEntity
            }
        }


        // 按顺序过滤剩余规则
        val remainingRules = logics.filter { logic -> !logic.contains(RaffleFilterFactory.LogicModel.RULE_BLACKLIST.code) }

        var ruleActionEntity: RuleActionEntity? = null
        for(rule in remainingRules){
            // 获取过滤器实体
            val ruleFilter = ruleFilters[rule]!!

            // 创建过滤器上下文
            val raffleFilterEntity = RaffleFilterEntity(
                userId = raffleInputEntity.userId,
                awardId = 0,
                strategyId = raffleInputEntity.strategyId,
                ruleModel = rule
            )

            // 获取过滤结果
            ruleActionEntity = ruleFilter.filter(raffleFilterEntity)

            // 如果被规则拦截
            if (ruleActionEntity.code != RuleLogicCheckTypeVO.ALLOW.code) {
                return ruleActionEntity
            }
        }

        ruleActionEntity?.let { return ruleActionEntity }?: run {throw AppException("Rule action is null.") }

    }
}