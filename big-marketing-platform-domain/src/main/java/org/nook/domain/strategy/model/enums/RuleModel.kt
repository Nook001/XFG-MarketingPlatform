package org.nook.domain.strategy.model.enums

// 规则模型枚举
// 定义系统中所有可用的规则类型
enum class RuleModel(val code: String, val info: String) {
    RULE_BLACKLIST("rule_blacklist", "黑名单规则"),
    RULE_WEIGHT("rule_weight", "权重规则"),
    RULE_LOCK("rule_lock", "解锁规则"),
    RULE_LUCK_AWARD("rule_luck_award", "兜底奖品规则"),
    RULE_RANDOM("rule_random", "随机值规则");
}