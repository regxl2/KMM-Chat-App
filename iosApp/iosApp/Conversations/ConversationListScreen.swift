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
    var body: some View {
        ScrollView(showsIndicators: false){
            LazyVStack{
                ForEach(viewModelAdapter.conversationsUi.conversations, id: \.conversationId){ conversation in
                    ConversationItem(conversation: conversation)
                }
            }
        }
        .task {
            await viewModelAdapter.startObserving()
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
