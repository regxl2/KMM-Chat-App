//
//  ChatViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 02/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared
import Combine

class ChatViewModelAdapter: ObservableObject{
    private let viewModel: ChatViewModel = ViewModelProvider.shared.chatViewModel
    
    @Published var chat: ChatUi = ChatUi(conversationId: "", conversationType: .chat, name: "chat", avatar: nil, messages: [])
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var text: String = ""
    
    init(conversationId: String, conversationType: ChatType, name: String){
        initializeChat(conversationId: conversationId, conversationType: conversationType, name: name)
        initStates()
    }
    
    @MainActor
    func observeState() async {
        await withTaskGroup(of: Void.self) { group in
            group.addTask { [weak self] in
                guard let self = self else { return }
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
            
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await error in self.viewModel.error {
                    await MainActor.run{
                        self.error = error
                    }
                }
            }
            
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await isLoading in self.viewModel.loading {
                    await MainActor.run{
                        self.isLoading = (isLoading as! Bool)
                    }
                }
            }

            group.addTask { [weak self] in
                guard let self = self else { return }
                for await text in self.viewModel.text {
                    await MainActor.run{
                        self.text = text
                    }
                }
            }
        }
    }
    
    func initStates(){
        viewModel.doInitStates()
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
    
    deinit{
        viewModel.clearStates()
        print("deinit chat")
    }
}
