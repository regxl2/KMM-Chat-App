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
    @EnvironmentObject private var contentViewModelAdapter: ContentViewModelAdapter
    @StateObject private var viewModelAdapter = NewConversationViewModelAdapter()

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
        .task{
            await viewModelAdapter.observeStates()
        }
        .searchable(text: Binding(
            get: { viewModelAdapter.query },
            set: { newQuery in viewModelAdapter.onQueryChange(newQuery: newQuery) }
        ), prompt: "Search Users")
    }
}

private extension NewConversation {
    @ViewBuilder
    func contentView() -> some View {
        switch onEnum(of: viewModelAdapter.searchUiState) {
        case .idle:
            EmptyView()
        case .loading:
            LoadingScreen()
        case .error(let error):
            ErrorScreen(text: error.message)
                .padding()
        case .result(let result):
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
        }
    }

    func handleUserSelection(user: User) {
        let conversationId = getConversationId(email: user.email, userId: contentViewModelAdapter.userId)
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

#Preview {
    NewConversation()
        .environmentObject(Navigation())
        .environmentObject(ContentViewModelAdapter())
}
