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
    @StateObject var viewModelAdpater: AddRoomMemberViewModelAdapter
    
    init(conversationId: String){
        _viewModelAdpater = StateObject(wrappedValue:AddRoomMemberViewModelAdapter(id:conversationId))
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
        .task{
            await viewModelAdpater.obeserveStates()
        }
        .searchable(text:
            Binding(
                get: { viewModelAdpater.query },
                set: { value in viewModelAdpater.onQueryChange(newQuery: value)}
            ),
            prompt: "Add Members"
        )
    }
}

extension AddRoomMember{
    @ViewBuilder
    func contentView() -> some View{
        switch onEnum(of: viewModelAdpater.searchUiState){
        case .idle:
            EmptyView()
        case .error(let error):
            ErrorScreen(text: error.message)
        case .loading:
            LoadingScreen()
        case .result(let result):
            ScrollView{
                LazyVStack{
                    ForEach(0..<result.users.count, id: \.self){ index in
                        let user = result.users[index]
                        AddRoomMemberItem(user: user){
                            viewModelAdpater.addUserToRoom(userId: user.email, index: index)
                        }
                    }
                }
            }
        }
    }
}
