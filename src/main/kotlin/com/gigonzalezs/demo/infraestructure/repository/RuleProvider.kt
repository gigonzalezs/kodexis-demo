package com.gigonzalezs.demo.infraestructure.repository

import org.kodexis.api.rules.compilers.IRuleCompiler
import org.kodexis.core.providers.YamlFileRuleProviderBase
import org.kodexis.core.singleResponse.SingleResponseRuleCompiler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RuleProvider(
    @Value("\${com.gigonzalezs.kodexis.demo.rulesPath}") val rulePath: String) : YamlFileRuleProviderBase(rulePath) {

    @Autowired
    private lateinit var singleResponseRuleCompiler: SingleResponseRuleCompiler

    override fun getRuleCompiler(): IRuleCompiler = singleResponseRuleCompiler
}