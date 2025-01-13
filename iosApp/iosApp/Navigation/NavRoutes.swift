//
//  NavRoutes.swift
//  Experiment
//
//  Created by Abhishek Rathore on 11/12/24.
//

import SwiftUI
import Shared

enum NavRoutes: Hashable, Codable{
    case SignIn
    case SignUp
    case ForgotPassword
    case OtpAccountVerify(email: String)
    case OtpPassVerify(email: String)
    case ResetPassword(email: String)
    case Conversations
    case Chat(conversationId: String, conversationType: String, name: String)
    case Loading
    case NewConversation
    case NewRoom
    case AddRoomMember(conversationId: String)
}

extension ContentView{
    @ViewBuilder
    func content(route: NavRoutes) -> some View{
        switch route {
        case .SignIn : SignIn()
        case .SignUp : SignUp()
        case .ForgotPassword : ForgotPassword()
        case .OtpAccountVerify(let email): OtpAccountVerify(email: email)
        case .OtpPassVerify(let email): OtpPassVerify(email: email)
        case .ResetPassword(let email): ResetPassword(email: email)
        case .Conversations: ConversationListScreen()
        case .Chat(let conversationId, let conversationType, let name): ChatView(
            conversationId: conversationId,
            conversationType: conversationType == "chat" ? ChatType.chat: ChatType.room,
            name: name)
        case .Loading: LoadingScreen()
        case .NewConversation: NewConversation()
        case .NewRoom: NewRoom()
        case .AddRoomMember(let conversationId): AddRoomMember(conversationId: conversationId)
        }
    }
}
