//
//  SendMessageView.swift
//  iosApp
//
//  Created by Abhishek Rathore on 24/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct SendMessageView: View {
    @Binding var text: String
    var onClickSend: ()-> Void
    var body: some View {
        HStack{
            TextField("Enter the message", text: $text)
                .padding(10)
                .frame(height: 50)
                .overlay{
                    RoundedRectangle(cornerRadius: 10).stroke(.gray, lineWidth: 1)
                }
            Button(){
                onClickSend()
            }
            label:{
                Image(systemName: "arrow.up.circle.fill")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 50, height: 50)
            }
        }
    }
}

