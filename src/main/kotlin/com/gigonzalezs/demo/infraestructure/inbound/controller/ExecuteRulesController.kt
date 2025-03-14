package com.gigonzalezs.demo.infraestructure.inbound.controller


import com.gigonzalezs.demo.domain.useCases.IApplyRulesOnFactsUseCase
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.kodexis.api.facts.DataEntry
import org.kodexis.api.facts.DataType
import org.kodexis.api.facts.Fact
import org.kodexis.api.rules.RuleAction
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

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

    private fun FactDTO.toFact(): Fact = Fact(type, parseDataEntries(data))

    private fun parseDataEntries(json: ObjectNode): HashMap<String, DataEntry> {
        val map = HashMap<String, DataEntry>()
        traverse(map, json, "")
        return map
    }

    private fun traverse(map: HashMap<String, DataEntry>, node: JsonNode, path: String) {
        when {
            node.isObject -> {
                node.fields().forEach { (key, value) ->
                    traverse(map, value, if (path.isEmpty()) key else "$path.$key")
                }
            }
            node.isArray -> {
                node.forEachIndexed { index, element ->
                    traverse(map, element, "$path[$index]")
                }
            }
            else -> {
                val dataType = determineDataType(node)
                map[path] = DataEntry(parseValue(node, dataType), dataType)
            }
        }
    }

    private fun determineDataType(node: JsonNode): DataType {
        return when {
            isValidDate(node.asText()) -> DataType.Date
            node.isBoolean -> DataType.Boolean
            node.isInt -> DataType.Int
            node.isDouble -> DataType.Double
            node.isLong -> DataType.Int
            node.isTextual -> DataType.String
            else -> DataType.String
        }
    }

    private fun isValidDate(value: String): Boolean {
        return try {
            LocalDateTime.parse(value)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    private fun parseValue(node: JsonNode, type: DataType): Any {
        return when (type) {
            DataType.String -> node.asText()
            DataType.Boolean -> node.asBoolean()
            DataType.Int -> node.asInt()
            DataType.Double -> node.asDouble()
            DataType.Date -> parseDate(node.asText())
        }
    }

    private fun parseDate(value: String): LocalDateTime {
        return try {
            LocalDateTime.parse(value)
        } catch (e: DateTimeParseException) {
            LocalDateTime.of(1900, 1, 1, 0, 0)
        }
    }
}
