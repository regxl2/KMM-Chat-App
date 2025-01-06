package org.example.project.kmmchat.presentation

import kotlinx.serialization.Serializable


@Serializable
sealed class NavRoutes{
    @Serializable
    data object Loading: NavRoutes()
    @Serializable
    data object Auth: NavRoutes()
    @Serializable
    data object SignUp: NavRoutes()
    @Serializable
    data object SignIn: NavRoutes()
    @Serializable
    data object ForgotPassword: NavRoutes()
    @Serializable
    data class OtpAccountVerify(val email: String): NavRoutes()
    @Serializable
    data class OtpPassVerify(val email: String): NavRoutes()
    @Serializable
    data class ResetPassword(val email: String): NavRoutes()
    @Serializable
    data object Conversations: NavRoutes()
    @Serializable
    data object NewConversation: NavRoutes()
    @Serializable
    data class Chat(val conversationId: String, val conversationType: String, val name: String): NavRoutes()
}