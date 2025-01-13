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
    @StateObject private var viewModelAdapter: ConversationsViewModelAdapter = ConversationsViewModelAdapter()
    @EnvironmentObject private var navigation: Navigation
    var body: some View {
        ZStack{
            ScrollView(showsIndicators: false){
                LazyVStack{
                    ForEach(viewModelAdapter.conversationsUi.conversations, id: \.conversationId){ conversation in
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
                        viewModelAdapter.logout()
                    }
                } label: {
                    Image(systemName: "ellipsis")
                        .foregroundColor(.primary)
                }
            }
        }
        .onAppear {
            viewModelAdapter.getConversationList()
        }
        .task{
            await viewModelAdapter.observeStates()
        }
        .refreshable {
            viewModelAdapter.getConversationList()
        }
        .navigationTitle("Conversations")
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    ConversationListScreen()
}
