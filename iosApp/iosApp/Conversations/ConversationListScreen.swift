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
        ZStack{
            ScrollView(showsIndicators: false){
                LazyVStack{
                    ForEach(viewModelAdapter.conversationsUi.conversations, id: \.conversationId){ conversation in
                        ConversationItem(conversation: conversation)
                    }
                }
            }
            VStack {
                            Spacer() // Push content to the bottom
                            
                            HStack {
                                Spacer() // Push content to the right
                                
                                // Floating button
                                Button(action: {
                                    print("Floating button tapped!")
                                }) {
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
