package org.nook.infrastructure.persistence.mapper

import org.apache.ibatis.annotations.Mapper
import org.nook.infrastructure.persistence.po.AwardPO


@Mapper
interface AwardMapper {
    fun get10Awards(): List<AwardPO>
}