package com.gigonzalezs.demo.infraestructure.repository

import com.gigonzalezs.demo.application.repository.IRulesRepository
import org.kodexis.api.IRuleExecutor
import org.kodexis.api.rules.IRulesProvider
import org.kodexis.core.FileRuleProvider
import org.kodexis.core.ExecutorFactory
import org.kodexis.core.YamlFileRuleProvider
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class RuleRepository : IRulesRepository {

    @Value("\${com.gigonzalezs.kodexis.demo.rulesPath}")
    private lateinit var rulePath: String
    @Autowired
    private lateinit var executorFactory: ExecutorFactory
    private var provider: FileRuleProvider? = null
    private val executorByName = HashMap<String, IRuleExecutor>()

    @PostConstruct
    private fun init() {
        //provider = FileRuleProvider(rulePath, ".drl")
        provider = YamlFileRuleProvider(rulePath)
    }

    private val ruleProvider: IRulesProvider
        get() = provider ?: throw IllegalStateException("Provider must not be null")

    override fun getRuleExecutor(name: String): IRuleExecutor =
         executorByName[name] ?: createRuleExecutor().also {
            executorByName[name] = it
        }

    private fun createRuleExecutor(): IRuleExecutor = executorFactory
        .withRulesProvider(ruleProvider)
        .useSingleResponse()
        .build()
}