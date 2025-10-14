package org.nook.infrastructure.persistent.mapper

import org.apache.ibatis.annotations.Mapper
import org.nook.infrastructure.persistent.po.Award


@Mapper
interface AwardMapper {
    fun get10Awards(): List<Award>
}