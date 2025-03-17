package com.gigonzalezs.demo.infraestructure.inbound.controller

import com.gigonzalezs.demo.domain.useCases.IApplyRulesOnFactsUseCase
import org.kodexis.api.facts.Fact
import org.kodexis.api.facts.FactBuilder
import org.kodexis.api.rules.RuleAction
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class ExecuteRulesController(
    private val executeRulesUseCase: IApplyRulesOnFactsUseCase
    ) {

    @PostMapping("/execute/{group}")
    @Throws(Exception::class)
    fun executeRules(@PathVariable("group") group: String,
                     @RequestBody factDTOs: Flux<FactDTO>): Mono<List<RuleAction>> =
        factDTOs.toFactCollection().applyRulesOnFacts(group)

    private fun Flux<FactDTO>.toFactCollection(): Mono<List<Fact>> =
        map { dto -> dto.toFact() }
        .collectList()

    private fun Mono<List<Fact>>.applyRulesOnFacts(rulesGroup: String): Mono<List<RuleAction>> =
        map { executeRulesUseCase.applyRules(rulesGroup, it) }

    private fun FactDTO.toFact(): Fact = FactBuilder()
        .withType(type)
        .addDataFromJson(data)
        .build()
}
