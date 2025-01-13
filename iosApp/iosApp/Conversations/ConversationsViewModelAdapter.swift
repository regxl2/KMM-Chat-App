////
////  ConversationsViewModelAdapter.swift
////  iosApp
////
////  Created by Abhishek Rathore on 02/01/25.
////  Copyright © 2025 orgName. All rights reserved.
////

import SwiftUI
import Shared

class ConversationsViewModelAdapter: ObservableObject {
    private let viewModel: ConversationsViewModel = ViewModelProvider.shared.conversationsViewModel
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var conversationsUi: ConversationsUI = ConversationsUI(conversations: [])
    
    func observeStates() async{
        await withTaskGroup(of: Void.self){ group in
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await isLoading in self.viewModel.isLoading{
                    await MainActor.run{
                        self.isLoading = isLoading as! Bool
                    }
                }
            }
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await error in self.viewModel.error{
                    await MainActor.run{
                        self.error = error
                    }
                }
            }
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await conversationsUi in self.viewModel.conversationUiState{
                    await MainActor.run{
                        self.conversationsUi = conversationsUi
                    }
                }
            }
        }
    }
    
    func logout(){
        viewModel.logout()
    }

    func getConversationList(){
        viewModel.getConversationList()
    }
    
    deinit{
        viewModel.clearStates()
    }

}
