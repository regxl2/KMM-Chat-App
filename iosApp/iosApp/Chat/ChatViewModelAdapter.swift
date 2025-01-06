//
//  ChatViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 02/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

class ChatViewModelAdapter: ObservableObject{
    private let viewModel: ChatViewModel = ViewModelProvider.shared.chatViewModel
    @Published var chat: ChatUi = ChatUi(conversationId: "", conversationType: .chat, name: "chat", avatar: nil, messages: [])
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var text: String = ""
    
    @MainActor
    func observeState() async {
        await withTaskGroup(of: Void.self) { group in
            group.addTask {
                for await state in self.viewModel.chat {
                    await MainActor.run{
                        self.chat = ChatUi(
                            conversationId: state.conversationId,
                            conversationType: state.conversationType,
                            name: state.name,
                            avatar: state.avatar,
                            messages: state.messages
                        )
                    }
                }
            }
            
            group.addTask {
                for await error in self.viewModel.error {
                    await MainActor.run{
                        self.error = error
                    }
                }
            }
            
            group.addTask {
                for await isLoading in self.viewModel.loading {
                    await MainActor.run{
                        self.isLoading = (isLoading as! Bool)
                    }
                }
            }

            group.addTask {
                for await text in self.viewModel.text {
                    await MainActor.run{
                        self.text = text
                    }
                }
            }
        }
    }
    
    func initializeChat(conversationId: String, conversationType: ChatType, name: String){
        viewModel.initializeChat(conversationId: conversationId, conversationType: conversationType, name: name)
    }
    
    func onTextChange(newText: String){
        viewModel.onTextChange(newText: newText)
    }
    
    func sendMessage(){
        viewModel.sendMessage()
    }
    
    func disconnect(){
        viewModel.disconnect()
    }
}
