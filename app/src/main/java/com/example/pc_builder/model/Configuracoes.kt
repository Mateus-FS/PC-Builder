package com.example.pc_builder.model

// temaEscuro == null -> o usuário ainda não escolheu; segue o tema do sistema.
data class Configuracoes(
    val temaEscuro: Boolean? = null
)