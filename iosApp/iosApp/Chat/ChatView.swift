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
    @ObservedObject var viewModel: ChatViewModel = ViewModelProvider.shared.chatViewModel
    
    init(conversationId: String, conversationType: ChatType, name: String){
        viewModel.initializeChat(conversationId: conversationId, conversationType: conversationType, name: name)
    }
    
    var body: some View {
        VStack{
            if(viewModel.loadingState){
                LoadingScreen()
            }
            if let error = viewModel.errorState {
                ErrorScreen(text: error)
            }
            else{
                ScrollViewReader{ proxy in
                    ScrollView(showsIndicators: false){
                        LazyVStack{
                            ForEach(viewModel.chatState.messages, id: \.self){ message in
                                MessageItem(message: message)
                            }
                        }
                        Color.clear
                            .frame(height: 1)
                            .id("bottomID")
                    }
                    .onChange(of: viewModel.chatState ) {
                        withAnimation {
                            proxy.scrollTo("bottomID", anchor: .bottom)
                        }
                    }
                }
            }
            SendMessageView(
                text: Binding(
                    get: { viewModel.textState },
                    set: { newText in viewModel.onTextChange(newText: newText)}
                ),
                onClickSend: { viewModel.sendMessage() }
            )
        }
        .onAppear{
            viewModel.doInitStates()
        }
        .onDisappear{
            viewModel.clearStates()
        }
        .navigationBarBackButtonHidden(true)
        .toolbar{
            ToolbarItem(placement: .topBarLeading){
                BackButton{
                    dismiss()
                    navigation.popBackStack()
                }
            }
            if viewModel.chatState.conversationType == .room {
                ToolbarItem(placement: .topBarTrailing){
                    Menu{
                        Button("Add members") {
                            navigation.navigateTo(
                                destination: NavRoutes.AddRoomMember(
                                    conversationId: viewModel.chatState.conversationId)
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
        .padding(8)
        .navigationTitle(viewModel.state(\.chat, equals: {$0 == $1}, mapper: { $0.name }))
        .navigationBarTitleDisplayMode(.inline)
    }
}

extension ChatViewModel{
    var chatState: ChatUi{
        get{
            return self.state(
                \.chat,
                 equals: { $0 == $1 },
                 mapper: { $0 }
            )
        }
    }
    var loadingState: Bool {
        get{
            return self.state(\.loading)
        }
    }
    var errorState: String? {
        get{
            return self.stateNullable(\.error)
        }
    }
    var textState: String {
        get{
            return self.state(\.text) ?? ""
        }
    }
}



#Preview {
    ChatView(conversationId: "123", conversationType: .chat, name: "John")
}
