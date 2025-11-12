package org.nook.infrastructure.persistence.mapper

import org.apache.ibatis.annotations.Mapper
import org.nook.infrastructure.persistence.po.StrategyPO

@Mapper
interface StrategyMapper {
    fun getByStrategyId(strategyId: Long): StrategyPO?
}