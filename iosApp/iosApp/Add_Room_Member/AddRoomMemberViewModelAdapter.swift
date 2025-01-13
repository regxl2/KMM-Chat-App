//
//  AddRoomMemberViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 13/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

class AddRoomMemberViewModelAdapter: ObservableObject {
    private let viewModel: AddRoomMemberViewModel = ViewModelProvider.shared.addRoomMemberViewModel
    @Published var query: String = ""
    @Published var searchUiState: SearchUiState = SearchUiState.Idle()
    
    init(id: String){
        setConversationId(id: id)
    }
 
    func obeserveStates() async{
        await withTaskGroup(of: Void.self){ group in
            group.addTask { [weak self] in
                guard let self = self else {return}
                for await query in self.viewModel.query{
                    await MainActor.run{
                        self.query = query
                    }
                }
            }
            group.addTask { [weak self] in
                guard let self = self else {return}
                for await searchUiState in self.viewModel.searchUiState{
                    await MainActor.run{
                        self.searchUiState = searchUiState
                    }
                }
            }
        }
    }
    
    func setConversationId(id: String){
        viewModel.setConversationId(id: id)
    }
    
    func onQueryChange(newQuery: String){
        viewModel.onQueryChange(newQuery: newQuery)
    }
    
    func addUserToRoom(userId: String, index: Int){
        viewModel.addUserToRoom(userId: userId, index: Int32(index))
    }
    
    deinit{
        viewModel.clearStates()
        print("deinit add room member")
    }
}

