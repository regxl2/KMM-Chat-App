package org.example.project.kmmchat.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.project.kmmchat.presentation.auth.authNav
import org.example.project.kmmchat.presentation.chat.Chat
import org.example.project.kmmchat.presentation.conversations.Conversations
import org.example.project.kmmchat.presentation.loading.Loading
import org.example.project.kmmchat.presentation.new_conversation.NewConversation

@Composable
fun MainNavigation(destination: Destination) {
    val navController = rememberNavController()
    val startDestination = when (destination) {
        Destination.AUTH -> NavRoutes.Auth
        Destination.CONVERSATIONS -> NavRoutes.Conversations
        Destination.LOADING -> NavRoutes.Loading
    }
    NavHost(navController = navController, startDestination = startDestination) {

        composable<NavRoutes.Loading> {
            Loading()
        }

        authNav(navController = navController)

        composable<NavRoutes.Conversations>(
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) {
            Conversations(
                onNewConversationClick = { navController.navigate(NavRoutes.NewConversation) },
                onConversationClick = { conversationId: String, conversationType: ChatType, name: String ->
                    navController.navigate(
                        NavRoutes.Chat(
                            conversationId = conversationId,
                            conversationType = when (conversationType) {
                                ChatType.CHAT -> "chat"
                                ChatType.ROOM -> "room"
                            },
                            name = name
                        )
                    )
                })
        }

        composable<NavRoutes.Chat>(
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() } ) { backStackEntry ->
            val chat = backStackEntry.toRoute<NavRoutes.Chat>()
            Chat(
                conversationId = chat.conversationId,
                conversationType = if (chat.conversationType == "chat") ChatType.CHAT else ChatType.ROOM,
                name = chat.name,
                onNavigateBack = { navController.popBackStack() })
        }

        composable<NavRoutes.NewConversation>(
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) {
            NewConversation(onNavigateBack = { navController.popBackStack() })
        }
    }
}

fun enterTransition(): EnterTransition {
    return slideInHorizontally()
}

fun exitTransition(): ExitTransition {
    return slideOutHorizontally { it + it / 2 }
}