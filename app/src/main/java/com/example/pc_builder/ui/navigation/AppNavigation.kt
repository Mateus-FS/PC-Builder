package com.example.pc_builder.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pc_builder.ui.componentes.detalhes.DetalhesComponenteScreen
import com.example.pc_builder.ui.componentes.formulario.FormularioComponenteScreen
import com.example.pc_builder.ui.componentes.lista.ListaComponentesScreen
import com.example.pc_builder.ui.configuracoes.ConfiguracoesScreen
import com.example.pc_builder.ui.montagens.editor.EditorMontagemScreen
import com.example.pc_builder.ui.montagens.lista.ListaMontagensScreen

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

private fun NavGraphBuilder.telaPrincipal(
    rota: String,
    conteudo: @Composable () -> Unit
) {
    composable(
        route = rota,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) { conteudo() }
}

@Composable
fun AppNavigation() {
    val nav = rememberNavController()

    val navegarPrincipal: (String) -> Unit = { rota ->
        nav.navigate(rota) {
            popUpTo(Rotas.LISTA) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(navController = nav, startDestination = Rotas.LISTA) {

        telaPrincipal(Rotas.LISTA) {
            ListaComponentesScreen(
                onAbrir = { nav.navigate(Rotas.detalhe(it)) },
                onCadastrar = { nav.navigate(Rotas.formulario()) },
                onNavegar = navegarPrincipal
            )
        }
        telaPrincipal(Rotas.MONTAGENS) {
            ListaMontagensScreen(
                onAbrir = { nav.navigate(Rotas.editorMontagem(it)) },
                onCriar = { nav.navigate(Rotas.editorMontagem()) },
                onNavegar = navegarPrincipal
            )
        }
        telaPrincipal(Rotas.CONFIGURACOES) {
            ConfiguracoesScreen(onNavegar = navegarPrincipal)
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
        composable(
            route = Rotas.EDITOR_MONTAGEM,
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) {
            EditorMontagemScreen(onVoltar = { nav.popBackStack() })
        }
    }
}