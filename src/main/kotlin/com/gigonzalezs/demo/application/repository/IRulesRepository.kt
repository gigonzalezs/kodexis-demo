package com.gigonzalezs.demo.application.repository

import org.kodexis.api.IRuleExecutor

interface IRulesRepository {

    fun getRuleExecutor(name: String) : IRuleExecutor
}