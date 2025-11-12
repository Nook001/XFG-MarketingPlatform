package org.nook.domain.strategy.service.rule

import org.nook.domain.strategy.annotation.RuleFilterAnnotation
import org.nook.domain.strategy.model.raffle.RaffleResultEntity
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

/**
 *
 */
@Service
class RaffleFilterFactory(
    logicFilters: List<IRuleFilter<*>>  // ← 构造函数参数：Spring 自动注入所有规则过滤器
) {

    // [规则名(code): 过滤器实例(BlackListFilter, WeightFilter)]
    val logicFilterMap = ConcurrentHashMap<String, IRuleFilter<RaffleResultEntity>>()

    // 读取所有过滤器 储存在Map中
    init {
        logicFilters.forEach { logic ->
            AnnotationUtils.findAnnotation(logic.javaClass, RuleFilterAnnotation::class.java)?.let {
                logicFilterMap[it.ruleModel.code] = logic
            }
        }
    }
}