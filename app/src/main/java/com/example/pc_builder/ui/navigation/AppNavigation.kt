package com.example.pc_builder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pc_builder.ui.componentes.detalhes.DetalhesComponenteScreen
import com.example.pc_builder.ui.componentes.formulario.FormularioComponenteScreen
import com.example.pc_builder.ui.componentes.lista.ListaComponentesScreen
import com.example.pc_builder.ui.montagens.editor.EditorMontagemScreen
import com.example.pc_builder.ui.montagens.lista.ListaMontagensScreen
import com.example.pc_builder.ui.configuracoes.ConfiguracoesScreen

object Rotas {
    const val LISTA = "componentes"
    const val DETALHE = "componentes/{id}"
    const val FORMULARIO = "componentes/formulario?id={id}"
    const val MONTAGENS = "montagens"
    const val EDITOR_MONTAGEM = "montagens/editor?id={id}"
    const val CONFIGURACOES = "configuracoes"

    fun detalhe(id: Long) = "componentes/$id"
    fun formulario(id: Long? = null) =
        if (id == null) "componentes/formulario" else "componentes/formulario?id=$id"
    fun editorMontagem(id: Long? = null) =
        if (id == null) "montagens/editor" else "montagens/editor?id=$id"
}

@Composable
fun AppNavigation() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rotas.LISTA) {
        composable(Rotas.LISTA) {
            ListaComponentesScreen(
                onAbrir = { nav.navigate(Rotas.detalhe(it)) },
                onCadastrar = { nav.navigate(Rotas.formulario()) },
                onAbrirMontagens = { nav.navigate(Rotas.MONTAGENS) },
                onAbrirConfiguracoes = { nav.navigate(Rotas.CONFIGURACOES) }   // NOVA
            )
        }
        composable(
            route = Rotas.DETALHE,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            DetalhesComponenteScreen(
                onVoltar = { nav.popBackStack() },
                onEditar = { id -> nav.navigate(Rotas.formulario(id)) }
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
        composable(Rotas.MONTAGENS) {
            ListaMontagensScreen(
                onVoltar = { nav.popBackStack() },
                onAbrir = { nav.navigate(Rotas.editorMontagem(it)) },
                onCriar = { nav.navigate(Rotas.editorMontagem()) }
            )
        }
        composable(
            route = Rotas.EDITOR_MONTAGEM,
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) {
            EditorMontagemScreen(onVoltar = { nav.popBackStack() })
        }
        composable(Rotas.CONFIGURACOES) {                                      // NOVA
            ConfiguracoesScreen(onVoltar = { nav.popBackStack() })
        }
    }
}