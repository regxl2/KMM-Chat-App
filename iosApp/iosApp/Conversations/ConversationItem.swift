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
    @Environment(Navigation.self) var navigation
    var body: some View {
        HStack(alignment: .center){
//            AsyncImage(url: URL(string: "")){phase in
//                if let image = phase.image{
//                    image
//                }
//                else{
//                    Color.gray
//                }
//            }
//                .frame(width: 50, height: 50)
//                .clipShape(Circle())
//                .padding(8)
            Circle()
                .fill(Color.gray)
                .frame(width: 50, height: 50)
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
//                if(conversation.unreadCount != 0){
//                    Text(String(conversation.unreadCount))
//                        .font(.caption)
//                        .padding(8)
//                        .background(Color.green)
//                        .clipShape(Circle())
//                }
//                else{
//                    Spacer()
//                        .padding(8)
//                }
            }
            .padding(8)
        }
        .onTapGesture {
            navigation.navigateTo(
                destination: NavRoutes.Chat(
                    conversationId: conversation.conversationId ,
                    conversationType: conversation.conversationType == .chat ? "chat" : "room",
                    name: conversation.name
                )
            )
        }
        .foregroundColor(Color.black)
        .frame(height: 66)
        .padding(EdgeInsets(top: 4, leading: 8, bottom: 4, trailing: 8))
    }
}

#Preview {
    let messageResponse: MessageResponseUI = MessageResponseUI(id: "1", content: "Hello world", contentType: .text, createdAt: "10:00, AM", isMine: false, senderId: "example@gmail.com", senderName: "example")
    let conversation: ConversationUI = ConversationUI(conversationId: "", conversationType: .chat, messageResponse: messageResponse, name: "")
    ConversationItem(conversation: conversation)
}
