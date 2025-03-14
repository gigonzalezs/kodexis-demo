package com.gigonzalezs.demo.domain.useCases

import org.kodexis.api.facts.Fact
import org.kodexis.api.rules.RuleAction
interface IApplyRulesOnFactsUseCase {
    fun applyRules(rulesGroup: String, facts: List<Fact>): List<RuleAction>
}
