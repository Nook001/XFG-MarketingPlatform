package org.nook.domain.strategy.annotation

import org.nook.domain.strategy.model.enums.RuleModel

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RuleFilterAnnotation(
    val ruleModel: RuleModel
)