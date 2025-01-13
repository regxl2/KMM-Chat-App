//
//  NewConversationViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 12/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

class NewConversationViewModelAdapter: ObservableObject {
    private let viewModel = ViewModelProvider.shared.newConversationViewModel
    @Published var query: String = ""
    @Published var searchUiState: SearchUiState = SearchUiState.Idle()
    
    func onQueryChange(newQuery: String){
        viewModel.onQueryChange(newQuery: newQuery)
    }
    
    @MainActor
    func observeStates() async{
        await withTaskGroup(of: Void.self){ group in
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await query in self.viewModel.query{
                    await MainActor.run{
                        self.query = query
                    }
                }
            }
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await searchUiState in self.viewModel.searchUiState{
                    await MainActor.run{
                        self.searchUiState = searchUiState
                    }
                }
            }
        }
    }
    
    deinit{
        onQueryChange(newQuery: "")
        print("deinit conversation new")
    }
}
