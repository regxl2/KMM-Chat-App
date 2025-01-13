//
//  ConversationItem.swift
//  iosApp
//
//  Created by Abhishek Rathore on 24/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct ConversationItem: View {
    let conversation: ConversationUI
    let onClick: () -> Void
    @EnvironmentObject private var navigation: Navigation
    var body: some View {
        HStack(alignment: .center){
            AvatarAlt(text: conversation.name.prefix(1).capitalized)
            VStack(alignment:.leading, spacing: 8){
                Text(conversation.name)
                    .fontWeight(.bold)
                Text(conversation.messageResponse?.content ?? "")
            }
            Spacer()
            VStack(alignment: .trailing, spacing: 8){
                if let timeStamp = conversation.messageResponse?.createdAt{
                    Text(timeStamp)
                }
                Spacer()
            }
            .padding(8)
        }
        .onTapGesture { onClick() }
        .foregroundColor(Color.black)
        .frame(height: 66)
        .padding(EdgeInsets(top: 4, leading: 8, bottom: 4, trailing: 8))
    }
}

#Preview {
    let messageResponse: MessageResponseUI = MessageResponseUI(id: "1", content: "Hello world", contentType: .text, createdAt: "10:00, AM", isMine: false, senderId: "example@gmail.com", senderName: "example")
    let conversation: ConversationUI = ConversationUI(conversationId: "", conversationType: .chat, messageResponse: messageResponse, name: "")
    ConversationItem(conversation: conversation, onClick: {})
}
