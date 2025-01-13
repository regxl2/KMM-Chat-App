//
//  UserBox.swift
//  iosApp
//
//  Created by Abhishek Rathore on 12/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct UserBox: View{
    let user: User
    let onClick: () -> Void
    
    var body: some View{
        HStack{
            AvatarAlt(text: user.name.prefix(1).uppercased())
            VStack(alignment: .leading){
                Text(user.name).fontWeight(.bold)
                Text(user.email)
            }
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: 66)
        .padding()
        .onTapGesture {
            onClick()
        }
    }
}
