package org.nook.domain.strategy.model.enums

enum class RuleLogicCheckTypeVO(
    val code: String,
    val info: String,
) {
    ALLOW("0000", "放行；执行后续流程，不收规则影响"),
    TAKE_OVER("0001","接管；后续流程由规则确定"),
}