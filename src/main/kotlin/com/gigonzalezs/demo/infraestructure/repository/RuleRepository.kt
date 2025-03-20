package com.gigonzalezs.demo.infraestructure.repository

import com.gigonzalezs.demo.application.repository.IRulesRepository
import org.kodexis.api.IRuleExecutor
import org.kodexis.core.ExecutorFactory
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Repository

@Repository
class RuleRepository : IRulesRepository {

    @Autowired
    private lateinit var singleResponseRuleProvider: SingleResponseRuleProvider

    @Autowired
    private lateinit var multiResponseProvider: MultiResponseRuleProvider

    @Autowired
    private lateinit var executorFactory: ExecutorFactory

    private val executorsByName = HashMap<String, IRuleExecutor>()

    override fun getRuleExecutor(name: String): IRuleExecutor =
         executorsByName[name] ?: createRuleExecutor(name).also {
            executorsByName[name] = it
        }

    private fun createRuleExecutor(name: String): IRuleExecutor = executorFactory
        .also {
            if (name.contains("multi")) {
                it.withRulesProvider(multiResponseProvider).useMultiResponse()

            } else {
                it.withRulesProvider(singleResponseRuleProvider).useSingleResponse()
            }
        }
        .build()
}