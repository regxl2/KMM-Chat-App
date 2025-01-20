package org.example.project.kmmchat.presentation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.example.project.kmmchat.presentation.NavRoutes
import org.example.project.kmmchat.presentation.enterTransition
import org.example.project.kmmchat.presentation.exitTransition

fun NavGraphBuilder.authNav(navController: NavHostController) {
    navigation<NavRoutes.Auth>(startDestination = NavRoutes.SignIn) {
        composable<NavRoutes.SignIn>(
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) {
            SignIn(
                navigateConversations = {
                    navController.navigate(NavRoutes.Conversations) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                navigateSignUp = { navController.navigate(NavRoutes.SignUp) },
                navigateForgotPassword = { navController.navigate(NavRoutes.ForgotPassword) }
            )
        }
        composable<NavRoutes.SignUp>(
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) {
            SignUp(
                navigateSignIn = { navController.popBackStack() },
                navigateAccountVerify = { email -> navController.navigate(NavRoutes.OtpAccountVerify(email)) }
            )
        }
        composable<NavRoutes.OtpAccountVerify>(
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) { navBackStack ->
            val email = navBackStack.arguments?.getString("email") ?: ""
            OtpAccountVerify(
                email = email,
                navigateSignIn = {
                    navController.navigate(NavRoutes.SignIn) {
                        popUpTo(NavRoutes.SignIn) {
                            saveState = true
                            inclusive = false
                        }
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable<NavRoutes.OtpPassVerify>(
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) { navBackStack ->
            val email = navBackStack.arguments?.getString("email") ?: ""
            OtpPassVerify(
                email = email,
                navigateResetPassword = {
                    navController.navigate(NavRoutes.ResetPassword(email = email)) {
                        popUpTo(NavRoutes.SignIn) {
                            saveState = true
                            inclusive = false
                        }
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable<NavRoutes.ForgotPassword>(
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }) {
            ForgotPasswordRoot(navController = navController)
        }
        composable<NavRoutes.ResetPassword>(
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() }
        ) { navBackStack ->
            val email = navBackStack.arguments?.getString("email") ?: ""
            ResetPassword(
                email = email,
                navigateSignIn = {
                    navController.navigate(NavRoutes.SignIn) {
                        popUpTo(NavRoutes.SignIn) {
                            saveState = true
                            inclusive = false
                        }
                    }
                },
                navigateOtpPassVerify = { navController.popBackStack() }
            )
        }
    }
}