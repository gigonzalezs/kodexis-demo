package com.gigonzalezs.demo.infraestructure.configuration

import com.gigonzalezs.demo.application.repository.IRulesRepository
import com.gigonzalezs.demo.application.usecCases.ApplyRulesOnFactsUseCase
import com.gigonzalezs.demo.domain.useCases.IApplyRulesOnFactsUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import org.kodexis.core.ExecutorFactory
import org.kodexis.core.multiResponse.MultiResponseRuleCompiler
import org.kodexis.core.singleResponse.SingleResponseRuleCompiler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeansConfiguration(
    private val objectMapper: ObjectMapper
) {
    @Bean
    fun getExecutorFactory(): ExecutorFactory = ExecutorFactory()

    @Bean
    fun getExecuteRulesUseCase(
        rulesRepository: IRulesRepository
    ) : IApplyRulesOnFactsUseCase = ApplyRulesOnFactsUseCase(rulesRepository)

    @Bean
    fun getSingleResponseRuleCompiler() = SingleResponseRuleCompiler()

    @Bean
    fun getMultiResponseRuleCompiler() = MultiResponseRuleCompiler()
}