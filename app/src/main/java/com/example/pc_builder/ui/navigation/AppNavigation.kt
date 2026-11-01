package com.example.pc_builder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pc_builder.ui.catalogo.DetalheComponenteScreen
import com.example.pc_builder.ui.catalogo.FormularioComponenteScreen
import com.example.pc_builder.ui.catalogo.ListaComponentesScreen

object Rotas {
    const val LISTA = "componentes"
    const val DETALHE = "componentes/{id}"
    const val FORMULARIO = "componentes/formulario?id={id}"

    fun detalhe(id: Long) = "componentes/$id"
    fun formulario(id: Long? = null) =
        if (id == null) "componentes/formulario" else "componentes/formulario?id=$id"
}

@Composable
fun AppNavigation() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rotas.LISTA) {
        composable(Rotas.LISTA) {
            ListaComponentesScreen(
                onAbrir = { nav.navigate(Rotas.detalhe(it)) },
                onCadastrar = { nav.navigate(Rotas.formulario()) }
            )
        }
        composable(
            route = Rotas.DETALHE,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            DetalheComponenteScreen(
                onVoltar = { nav.popBackStack() },
                onEditar = { nav.navigate(Rotas.formulario(it)) }
            )
        }
        composable(
            route = Rotas.FORMULARIO,
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) {
            FormularioComponenteScreen(onVoltar = { nav.popBackStack() })
        }
    }
}