//
//  ChatView.swift
//  iosApp
//
//  Created by Abhishek Rathore on 24/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared


struct ChatView: View {

    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) var dismiss
    @StateObject private var viewModelAdapter: ChatViewModelAdapter
    
    init(conversationId: String, conversationType: ChatType, name: String){
        _viewModelAdapter = StateObject(wrappedValue: ChatViewModelAdapter(conversationId: conversationId, conversationType: conversationType, name: name))
    }
    
    var body: some View {
        VStack{
            if(viewModelAdapter.isLoading){
                LoadingScreen()
            }
            if let error = viewModelAdapter.error{
                ErrorScreen(text: error)
            }
            else{
                ScrollViewReader{ proxy in
                    ScrollView(showsIndicators: false){
                        LazyVStack{
                            ForEach(viewModelAdapter.chat.messages, id: \.self){ message in
                                MessageItem(message: message)
                            }
                        }
                        Color.clear
                            .frame(height: 1)
                            .id("bottomID")
                    }
                    .onChange(of: viewModelAdapter.chat.messages.count) {
                        withAnimation {
                            proxy.scrollTo("bottomID", anchor: .bottom)
                        }
                    }
                }
            }
            SendMessageView(
                text: Binding(
                    get: {viewModelAdapter.text},
                    set: { newText in viewModelAdapter.onTextChange(newText: newText)}
                ),
                onClickSend: {viewModelAdapter.sendMessage()})
        }
        .navigationBarBackButtonHidden(true)
        .toolbar{
            ToolbarItem(placement: .topBarLeading){
                BackButton{
                    dismiss()
                    navigation.popBackStack()
                }
            }
            if viewModelAdapter.chat.conversationType == .room {
                ToolbarItem(placement: .topBarTrailing){
                    Menu{
                        Button("Add members") {
                            navigation.navigateTo(
                                destination: NavRoutes.AddRoomMember(
                                    conversationId: viewModelAdapter.chat.conversationId)
                            )
                        }
                    }
                    label: {
                        Image(systemName: "ellipsis")
                            .foregroundColor(.primary)
                    }
                    
                }
            }
        }
        .task{
            await viewModelAdapter.observeState()
        }
        .padding(8)
        .navigationTitle(viewModelAdapter.chat.name)
        .navigationBarTitleDisplayMode(.inline)
    }
}


#Preview {
    ChatView(conversationId: "123", conversationType: .chat, name: "John")
}
