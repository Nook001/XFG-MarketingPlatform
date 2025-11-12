package org.nook.domain.strategy.service.rule.filter

import com.alibaba.fastjson2.JSON
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


@Component
@RuleFilterAnnotation(RuleModel.RULE_WEIGHT)
class RuleWeightFilter(
    private val repository: IRepository
): IRuleFilter<RaffleResultEntity> {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(RuleWeightFilter::class.java)
    }


    private var userPoints = 4500L

    override fun filter(raffleFilterEntity: RaffleFilterEntity): RuleActionEntity {
        val userId: String = raffleFilterEntity.userId
        val strategyId = raffleFilterEntity.strategyId
        val ruleModel = raffleFilterEntity.ruleModel

        // 日志
        log.info(
            "开始过滤: 规则类型[{}] userID[{}] strategyID[{}] ruleModel[{}]",
            "权重值", userId, strategyId, ruleModel
        )


        // 从数据库获取策略级规则（权重值）规则值
        // Rule_Value 4000:102,103,104,105;5000:102,103,104,105,106,107;6000:102,103,104,105,106,107,108
        val rule: StrategyRuleEntity = repository.getType1StrategyRuleEntity(strategyId, ruleModel)

        // 分离规则值Map[key1: [value1,value2...], key2: [value1,...], ...]
        // [4000:[102,103,104,105]], [5000:[102,103,104,105,106,107], [6000:102,103,104,105,106,107,108]
        val ruleEntry: Map<String, List<String>> = rule.ruleValue.split(Constants.Split.SEMICOLON)
            .associate { entry ->
                val (key, valuesString) = entry.split(Constants.Split.COLON)
                val valuesList = valuesString.split(Constants.Split.COMMA)

                // 'to' 关键字创建 Pair<String, List<String>>
                key to valuesList
            }

        log.info("规则值: ${JSON.toJSONString(ruleEntry)}")

        // 如果规则值为空
        if (ruleEntry.isEmpty()) {
            return RuleActionEntity(
                code = RuleLogicCheckTypeVO.ALLOW.code,
                info = RuleLogicCheckTypeVO.ALLOW.info,
            )
        }

        // 找到最小的符合值
        // userPoints = 4000
        // [3600: 101],[4000: 102],[4050: 103] 返回 [3600: 101]
        val minSatisfiedEntry: Map.Entry<String, List<String>>? = ruleEntry.entries
            .filter { it.key.toInt() <= userPoints }    // 找出所有满足的规则值
            .minByOrNull { it.key.toInt() }    // 找到条件值最小的


        // 如果找到匹配值,接管
        return minSatisfiedEntry?.let {
            log.info("匹配值: ${minSatisfiedEntry.key}: ${minSatisfiedEntry.value}")
             RuleActionEntity (
                 code = RuleLogicCheckTypeVO.TAKE_OVER.code,
                 info = RuleLogicCheckTypeVO.TAKE_OVER.info,
                 ruleModel = RuleModel.RULE_WEIGHT.code,
                 data = RaffleResultEntity(
                     strategyId = strategyId,
                     stage = RaffleStage.BEFORE,
                     ruleWeightValueKey = minSatisfiedEntry.key,  // ← 修正：传权重值的 key (如 "4000")
                 )
            )
        // 没有匹配, 通过
        } ?: run {
            log.info("没有匹配值")
            RuleActionEntity(
              code = RuleLogicCheckTypeVO.ALLOW.code,
              info = RuleLogicCheckTypeVO.ALLOW.info,
            )
        }
    }
}