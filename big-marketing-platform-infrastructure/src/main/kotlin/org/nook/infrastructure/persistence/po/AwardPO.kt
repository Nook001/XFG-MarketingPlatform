package org.nook.infrastructure.persistence.po

import java.time.LocalDateTime

data class AwardPO(
    var id: Long? = null,
    var awardId: Int,               // 奖品ID
    var awardKey: String,          // 奖品标识
    var awardConfig: String,       // 奖品配置
    var awardDesc: String,        // 奖品描述
    var createTime: LocalDateTime,  // 创建时间
    var updateTime: LocalDateTime   // 修改时间
)
