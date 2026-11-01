package com.example.pc_builder.util

import java.text.NumberFormat
import java.util.Locale

private val localeBr = Locale("pt", "BR")

fun formatarPreco(valor: Double): String =
    NumberFormat.getCurrencyInstance(localeBr).format(valor)

fun precoParaCampo(valor: Double): String =
    String.format(localeBr, "%.2f", valor)   // 549,00