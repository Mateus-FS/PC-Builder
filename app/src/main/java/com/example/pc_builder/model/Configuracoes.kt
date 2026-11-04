package com.example.pc_builder.model

data class Configuracoes(
    val temaEscuro: Boolean? = null,

    // Orçamento padrão definido pelo usuário
    val orcamentoPadrao: Double = 0.0,

    // Forma de ordenação dos componentes
    val ordenacaoPreco: String = "NOME"
)