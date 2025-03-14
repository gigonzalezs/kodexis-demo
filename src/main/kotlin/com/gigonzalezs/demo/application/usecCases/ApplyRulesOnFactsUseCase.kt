package com.gigonzalezs.demo.application.usecCases

import com.gigonzalezs.demo.application.repository.IRulesRepository
import com.gigonzalezs.demo.domain.useCases.IApplyRulesOnFactsUseCase
import org.kodexis.api.facts.Fact
import org.kodexis.api.rules.RuleAction

class ApplyRulesOnFactsUseCase (

    private val rulesRepository: IRulesRepository
    ) : IApplyRulesOnFactsUseCase {

    override fun applyRules(rulesGroup: String, facts: List<Fact>): List<RuleAction> =
        rulesRepository.getRuleExecutor(rulesGroup).executeRules(facts)

}