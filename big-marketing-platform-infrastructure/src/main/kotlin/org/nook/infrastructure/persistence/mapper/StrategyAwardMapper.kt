package org.nook.infrastructure.persistence.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.nook.infrastructure.persistence.po.StrategyAwardPO

@Mapper
interface StrategyAwardMapper {
    fun getStrategyAwardByStrategyId(
        @Param("strategyId") strategyId: Long
    ): List<StrategyAwardPO>
}