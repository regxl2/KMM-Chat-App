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
    
    init(){
        print("init")
    }
    
    @MainActor
    func startObserving() async{
        await withTaskGroup(of: Void.self){ group in
            group.addTask {
                for await isLoading in self.viewModel.isLoading{
                    await MainActor.run{
                        self.isLoading = isLoading as! Bool
                    }
                }
            }
            group.addTask {
                for await error in self.viewModel.error{
                    await MainActor.run{
                        self.error = error
                    }
                }
            }
            group.addTask {
                for await conversationsUi in self.viewModel.conversationUiState{
                    await MainActor.run{
                        self.conversationsUi = conversationsUi
                    }
                }
            }
        }
    }

    func getConversationList() {
        viewModel.getConversationList()
    }
    
    deinit{
        print("deinit")
    }

}
