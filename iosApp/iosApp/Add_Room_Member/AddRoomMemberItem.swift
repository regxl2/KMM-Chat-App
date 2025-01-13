//
//  AddRoomMemberItem.swift
//  Experiment
//
//  Created by Abhishek Rathore on 13/01/25.
//
import SwiftUI
import Shared

struct AddRoomMemberItem: View {
    let user: User
    let addAction: () -> Void
    var body: some View {
        HStack{
            AvatarAlt(text: "A")
            HStack{
                VStack(alignment: .leading){
                    Text(user.name)
                        .fontWeight(.bold)
                    Spacer()
                    Text(user.email)
                }
                Spacer()
                Button(action: {
                    addAction()
                }){
                    Text("Add")
                }
                .padding()
                .disabled(user.isRoomMember)
            }
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: 66)
    }
}

#Preview {
    let user = User(email: "example@gmail.com", name: "example", isRoomMember: false)
    AddRoomMemberItem(user: user, addAction: {  } )
}
