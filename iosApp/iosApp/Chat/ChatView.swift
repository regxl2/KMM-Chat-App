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
    let conversationId: String
    let conversationType: ChatType
    let name: String
    @Environment(Navigation.self) var navigation
    @StateObject private var viewModelAdapter: ChatViewModelAdapter = ChatViewModelAdapter()
    
    var body: some View {
        VStack{
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
            SendMessageView(
                text: Binding(
                    get: {viewModelAdapter.text},
                    set: { newText in viewModelAdapter.onTextChange(newText: newText)}
                ),
                onClickSend: {viewModelAdapter.sendMessage()})
        }
        .navigationBarBackButtonHidden()
        .toolbar{
            ToolbarItem(placement: .navigationBarLeading){
                BackButton{
                    navigation.navigateBack()
                }
            }
        }
        .onAppear{
            viewModelAdapter.initializeChat(conversationId: conversationId, conversationType: conversationType, name: name)
        }
        .task {
            await viewModelAdapter.observeState()
        }
        .onDisappear{
            viewModelAdapter.disconnect()
        }
        .padding(8)
        .navigationTitle(viewModelAdapter.chat.name)
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    ChatView(conversationId: "123", conversationType: .chat, name: "John")
}
