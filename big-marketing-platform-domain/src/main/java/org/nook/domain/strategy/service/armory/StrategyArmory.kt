package org.nook.domain.strategy.service.armory

import jakarta.annotation.Resource
import org.nook.domain.strategy.model.entity.StrategyAwardEntity
import org.nook.domain.strategy.model.entity.StrategyEntity
import org.nook.domain.strategy.model.entity.StrategyRuleEntity
import org.nook.domain.strategy.repository.IRepository
import org.nook.types.common.Constants
import org.nook.types.exception.AppException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode


@Service
class StrategyArmory: IStrategyArmory {

    @Resource
    private lateinit var repository: IRepository

    override fun assembleStrategy(strategyId: Long) {
        // 1. 组装基础概率表：查询策略的所有奖品，构建默认概率表
        val strategyAwardList: List<StrategyAwardEntity> = repository.getStrategyAwardEntity(strategyId)
        assembleProbabilityTable(strategyId.toString(), strategyAwardList)

        // 2. 组装权重概率表：如果配置了权重规则，为不同权重值构建专属概率表
        val strategyEntity: StrategyEntity = repository.getStrategyEntity(strategyId)
        // 检查是否启用了权重规则
        if (!strategyEntity.getRuleModelList().contains(Constants.RuleModel.WEIGHT)) return

        // 获取权重规则配置，例如："4000:102,103,104,105;5000:102,103,104,105,106,107"
        val strategyRuleEntity: StrategyRuleEntity = repository.getType1StrategyRuleEntity(strategyId, Constants.RuleModel.WEIGHT)
        val ruleWeightValues = strategyRuleEntity.getRuleWeightValues()
        
        // 为每个权重值构建专属概率表
        // 例如: key="4000" → 只包含奖品[102,103,104,105]的概率表
        for ((weightKey, awardIds) in ruleWeightValues) {
            val filteredAwardList: List<StrategyAwardEntity> = strategyAwardList.filter { it.awardId in awardIds }
            assembleProbabilityTable("$strategyId:$weightKey", filteredAwardList)
        }
    }


    /**
     * 构建概率查询表(Redis)
     * 
     * 核心算法：将概率转换为查询表，实现 O(1) 时间复杂度的概率抽奖
     * 
     * 例如：
     * - 奖品A概率0.8，奖品B概率0.2
     * - 最小概率0.2，总概率1.0
     * - 表大小 = 1.0/0.2 = 5
     * - 结果表：[A, A, A, A, B]（A出现4次，B出现1次）
     * - 随机取一个索引，即可按概率获得奖品
     * 
     * @param key 存储key，格式：strategyId 或 strategyId:weightKey
     * @param list 奖品列表及其概率配置
     */
    private fun assembleProbabilityTable(key: String, list: List<StrategyAwardEntity>) {
        // 1. 查找最小概率（用于计算表的基准单位）
        val minAwardRate: BigDecimal = list.minOfOrNull { it.awardRate } ?: run {
            throw AppException("400", "策略[$key]的奖品概率配置无效")
        }

        // 2. 计算总概率（所有奖品概率之和）
        val totalAwardRate: BigDecimal = list.sumOf { it.awardRate }

        // 3. 计算概率表大小（向上取整，确保所有奖品都能分配到位置）
        // 公式：tableSize = totalRate / minRate
        // 例如：总概率1.976，最小0.001，表大小=1976
        val rateRange: BigDecimal = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING)

        // 4. 构建概率查询表（预分配容量以提高性能）
        val strategyAwardSearchTable: MutableList<Int> = ArrayList(rateRange.toInt())
        
        // 5. 填充概率表：每个奖品按其概率比例重复出现
        for (item in list) {
            val awardId: Int = item.awardId
            val awardRate: BigDecimal = item.awardRate

            // 计算该奖品在表中应该出现的次数
            // 例如：奖品概率0.8，最小0.001，出现次数=0.8/0.001=800次
            val count = awardRate.divide(minAwardRate, 0, RoundingMode.CEILING).toInt()
            repeat(count) {
                strategyAwardSearchTable.add(awardId)
            }
        }

        // 6. 随机打乱表顺序，避免奖品分布出现规律性
        strategyAwardSearchTable.shuffle()

        // 7. 存储到Redis缓存，供抽奖时快速查询
        repository.storeProbabilityTable(
            key, 
            strategyAwardSearchTable.size.toBigDecimal(), 
            strategyAwardSearchTable
        )
    }
}