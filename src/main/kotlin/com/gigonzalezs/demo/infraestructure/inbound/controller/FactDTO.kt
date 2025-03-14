package com.gigonzalezs.demo.infraestructure.inbound.controller

import com.fasterxml.jackson.databind.node.ObjectNode

class FactDTO (
    val type: String,
    val data: ObjectNode
)


