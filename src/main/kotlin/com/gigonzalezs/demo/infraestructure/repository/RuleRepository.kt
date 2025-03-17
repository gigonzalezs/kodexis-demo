package com.gigonzalezs.demo.infraestructure.repository

import com.gigonzalezs.demo.application.repository.IRulesRepository
import org.kodexis.api.IRuleExecutor
import org.kodexis.core.ExecutorFactory
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Repository

@Repository
class RuleRepository : IRulesRepository {

    @Autowired
    private lateinit var provider: RuleProvider

    @Autowired
    private lateinit var executorFactory: ExecutorFactory

    private val executorsByName = HashMap<String, IRuleExecutor>()

    override fun getRuleExecutor(name: String): IRuleExecutor =
         executorsByName[name] ?: createRuleExecutor().also {
            executorsByName[name] = it
        }

    private fun createRuleExecutor(): IRuleExecutor = executorFactory
        .withRulesProvider(provider)
        .useSingleResponse()
        .build()
}