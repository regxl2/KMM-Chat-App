//
//  AddRoomMember.swift
//  iosApp
//
//  Created by Abhishek Rathore on 13/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct AddRoomMember: View{
    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject private var navigation: Navigation
    @ObservedObject private var viewModel = ViewModelProvider.shared.addRoomMemberViewModel
    
    init(conversationId: String){
        viewModel.setConversationId(id: conversationId)
    }
    
    var body: some View{
        ZStack{
            contentView()
        }
        .navigationBarBackButtonHidden(true)
        .navigationTitle("Add Members")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar{
            ToolbarItem(placement: .topBarLeading){
                BackButton{
                    dismiss()
                    navigation.popBackStack()
                }
            }
        }
        .onDisappear{
            viewModel.resetStates()
        }
        .searchable(text:
            Binding(
                get: { viewModel.queryState },
                set: { value in viewModel.onQueryChange(newQuery: value)}
            ),
            prompt: "Add Members"
        )
    }
}

extension AddRoomMemberViewModel{
    var queryState: String {
        get{
            return self.state(\.query) ?? ""
        }
    }
    var searchState: SearchUiState{
        get{
            return self.state(
                \.searchUiState,
                 equals: { $0 == $1 },
                 mapper: { $0 }
            )
        }
    }
}

extension AddRoomMember{
    @ViewBuilder
    func contentView() -> some View{
        switch viewModel.searchState{
        case _ as SearchUiState.Idle:
            EmptyView()
        case let error as SearchUiState.Error:
            ErrorScreen(text: error.message)
        case _ as SearchUiState.Loading:
            LoadingScreen()
        case let result as SearchUiState.Result:
            ScrollView{
                LazyVStack{
                    ForEach(0..<result.users.count, id: \.self){ index in
                        let user = result.users[index]
                        AddRoomMemberItem(user: user){
                            viewModel.addUserToRoom(userId: user.email, index: Int32(index))
                        }
                    }
                }
            }
        default:
            ErrorScreen(text: "Unknown Error")
        }
    }
}
