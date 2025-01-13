//
//  MessageItem.swift
//  iosApp
//
//  Created by Abhishek Rathore on 24/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct MessageItem: View {
    let message: MessageResponseUI
    var body: some View {
        HStack(alignment: .top){
            if(!message.isMine){
                AvatarAlt(text: message.senderName.prefix(1).capitalized)
            }
            else{
                Spacer()
            }
            VStack(alignment: .leading, spacing: 4){
                if(!message.isMine){
                    Text(message.senderName)
                        .fontWeight(.bold)
                }
                switch message.contentType{
                case .image:
                    AsyncImage(url: URL(string: message.content), content: { phase in
                        if let image = phase.image{
                            image
                                .resizable()
                                .scaledToFit()
                                .frame(width: 100)
                        }
                        else {
                            Image(systemName: "person.fill")
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 50, height: 50)
                                .background(Color.red.opacity(0.3))
                                .clipShape(Circle())
                        }
                    })
                    placeholder: do {
                        ProgressView()
                    }
                case .text:
                    Text(message.content)
                        .padding(.all, 8)
                        .background(Color.gray.opacity(0.2))
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                }
                Text(message.createdAt).font(Font.system(size: 12))
            }
            if(!message.isMine){
                Spacer()
            }
        }
        .frame(maxWidth: .infinity)
    }
}

#Preview {
    let message: MessageResponseUI  = MessageResponseUI(
        id: "1",
        content: "Hey, how are you?",
        contentType: .text,
        createdAt: "9:11, AM",
        isMine: false,
        senderId: "123",
        senderName: "John Dow"
    )
    MessageItem(message: message)
}
