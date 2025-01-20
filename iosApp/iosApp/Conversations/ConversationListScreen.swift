//
//  ConversationListScreen.swift
//  iosApp
//
//  Created by Abhishek Rathore on 24/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct ConversationListScreen: View {
    @ObservedObject private var viewModel = ViewModelProvider.shared.conversationsViewModel
    @EnvironmentObject private var navigation: Navigation
    
    var body: some View {
        ZStack{
            ScrollView(showsIndicators: false){
                LazyVStack{
                    ForEach(viewModel.conversationState.conversations, id: \.conversationId){ conversation in
                        ConversationItem(conversation: conversation){
                            navigation.navigateTo(
                                destination: NavRoutes.Chat(
                                    conversationId: conversation.conversationId ,
                                    conversationType: conversation.conversationType == .chat ? "chat" : "room",
                                    name: conversation.name
                                )
                            )
                        }
                    }
                }
            }
            VStack {
                Spacer()
                HStack {
                    Spacer()
                    Button(action: {
                        navigation.navigateTo(destination: NavRoutes.NewConversation)
                    }){
                        Image(systemName: "plus")
                            .font(.system(size: 24, weight: .bold))
                            .foregroundColor(.white)
                            .padding()
                            .background(Color.blue)
                            .clipShape(Circle())
                            .shadow(color: .gray, radius: 4, x: 0, y: 4)
                    }
                    .padding()
                }
            }
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Menu {
                    Button("New Room") {
                        navigation.navigateTo(destination: NavRoutes.NewRoom)
                    }
                    Button("Logout") {
                        viewModel.resetStates()
                        viewModel.logout()
                    }
                } label: {
                    Image(systemName: "ellipsis")
                        .foregroundColor(.primary)
                }
            }
        }
        .onAppear{
            viewModel.getConversationList()
        }
        .refreshable {
            viewModel.getConversationList()
        }
        .navigationTitle("Conversations")
        .navigationBarTitleDisplayMode(.inline)
    }
}

extension ConversationsViewModel{
    var conversationState: ConversationsUI{
        get{
            return self.state(
                \.conversationUiState,
                 equals: { $0 == $1 },
                 mapper: { $0 }
            )
        }
    }
    var loadingState: Bool{
        get{
            return self.state(\.isLoading)
        }
    }
    var errorState: String? {
        get{
            return self.stateNullable(\.error)
        }
    }
}

#Preview {
    ConversationListScreen()
}
