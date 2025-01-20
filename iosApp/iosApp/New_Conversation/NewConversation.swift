//
//  New_Conversation.swift
//  iosApp
//
//  Created by Abhishek Rathore on 12/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct NewConversation: View {
    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject private var mainViewModel: MainViewModel
    @ObservedObject private var viewModel = ViewModelProvider.shared.newConversationViewModel

    var body: some View {
        ZStack{
            contentView()
        }
        .navigationBarBackButtonHidden(true)
        .navigationTitle("New Conversation")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
                BackButton {
                    dismiss()
                    navigation.popBackStack()
                }
            }
        }
        .searchable(text: Binding(
            get: { viewModel.queryState },
            set: { newQuery in viewModel.onQueryChange(newQuery: newQuery) }
        ), prompt: "Search Users")
    }
}

private extension NewConversation {
    @ViewBuilder
    func contentView() -> some View {
        switch viewModel.searchState {
        case _ as SearchUiState.Idle:
            EmptyView()
        case _ as SearchUiState.Loading:
            LoadingScreen()
        case let error as SearchUiState.Error:
            ErrorScreen(text: error.message)
        case let result as SearchUiState.Result:
            ScrollView {
                LazyVStack {
                    ForEach(result.users, id: \.email) { user in
                        UserBox(
                            user: user,
                            onClick: {
                                handleUserSelection(user: user)
                            }
                        )
                    }
                }
            }
        default: ErrorScreen(text: "Unknown error")
        }
    }

    func handleUserSelection(user: User) {
        let conversationId = getConversationId(email: user.email, userId: mainViewModel.userIdState)
        navigation.navigateTo(
            destination: NavRoutes.Chat(
                conversationId: conversationId,
                conversationType: "chat",
                name: user.name
            )
        )
    }

    func getConversationId(email: String, userId: String) -> String {
        return [email, userId].sorted().joined(separator: "-")
    }
}

extension MainViewModel{
    var userIdState: String {
        get{
            return self.state(\.userId) ?? ""
        }
    }
}

extension NewConversationViewModel{
    var queryState: String {
        get{
            return self.state(\.query) ?? ""
        }
    }
    var searchState: SearchUiState{
        get{
            return self.state(
                \.searchUiState,
                 equals: { $0 == $1 },
                 mapper: { $0 }
            )
        }
    }
}

#Preview {
    @ObservedObject var viewModel = ViewModelProvider.shared.newConversationViewModel
    NewConversation()
        .environmentObject(Navigation())
        .environmentObject(viewModel)
}
