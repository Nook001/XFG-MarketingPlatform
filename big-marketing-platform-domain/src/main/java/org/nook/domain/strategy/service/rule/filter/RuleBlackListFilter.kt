package org.nook.domain.strategy.service.rule.filter

import org.nook.domain.strategy.annotation.RuleFilterAnnotation
import org.nook.domain.strategy.model.raffle.RuleActionEntity
import org.nook.domain.strategy.model.raffle.RaffleFilterEntity
import org.nook.domain.strategy.model.raffle.RaffleResultEntity
import org.nook.domain.strategy.model.enums.RaffleStage
import org.nook.domain.strategy.model.entity.StrategyRuleEntity
import org.nook.domain.strategy.model.enums.RuleModel
import org.nook.domain.strategy.model.enums.RuleLogicCheckTypeVO
import org.nook.domain.strategy.repository.IRepository
import org.nook.domain.strategy.service.rule.IRuleFilter
import org.nook.types.common.Constants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * 黑名单规则过滤器
 * 
 * 功能：检查用户是否在黑名单中
 * - 如果在黑名单：返回兜底奖品（通常是1积分）
 * - 如果不在黑名单：放行，继续执行后续规则
 */
@Component
@RuleFilterAnnotation(RuleModel.RULE_BLACKLIST)
class RuleBlackListFilter(
    private val repository: IRepository  // ✅ 构造函数注入
) : IRuleFilter<RaffleResultEntity> {  // ✅ 注意：interface 后面没有括号

    companion object {
        private val log: Logger = LoggerFactory.getLogger(RuleBlackListFilter::class.java)
    }

    override fun filter(raffleFilterEntity: RaffleFilterEntity): RuleActionEntity {
        val userId: String = raffleFilterEntity.userId
        val strategyId = raffleFilterEntity.strategyId
        val ruleModel = raffleFilterEntity.ruleModel

        // 日志
        log.info(
            "开始过滤: 规则类型[{}] userID[{}] strategyID[{}] ruleModel[{}]",
            "黑名单", userId, strategyId, ruleModel
        )

        // 从数据库获取策略级规则（黑名单）规则值
        // Rule_Value 100:user001,user002,user003
        val rule: StrategyRuleEntity = repository.getType1StrategyRuleEntity(strategyId, ruleModel)

        // 分离规则值
        // [100],[user001,user002,user003]
        val ruleValue: List<String> = rule.ruleValue.split(Constants.Split.COLON)

        // 分离出 黑名单awardID
        // [100]
        val awardId = ruleValue[0].toInt()

        // 分离出 黑名单用户id列表
        // [user001,user002,...]
        val blackList: List<String> = ruleValue[1].split(Constants.Split.COMMA)

        // 检测用户是否在黑名单内
        if (blackList.isNotEmpty() and blackList.contains(userId)) {
            // 在黑名单，给兜底奖品（1积分）
            log.info("userID[{}]在黑名单内", userId)
            return RuleActionEntity(
                code = RuleLogicCheckTypeVO.TAKE_OVER.code,
                info = RuleLogicCheckTypeVO.TAKE_OVER.info,
                ruleModel = RuleModel.RULE_BLACKLIST.code,
                data = RaffleResultEntity(strategyId = strategyId, stage = RaffleStage.BEFORE, awardId = awardId),
            )
        } else {
            // 不在黑名单，通过
            log.info("userID[{}]不在黑名单内", userId)
            return RuleActionEntity(
                code = RuleLogicCheckTypeVO.ALLOW.code,
                info = RuleLogicCheckTypeVO.ALLOW.info,
            )
        }

    }
}