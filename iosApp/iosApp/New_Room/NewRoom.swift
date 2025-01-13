//
//  NewRoom.swift
//  iosApp
//
//  Created by Abhishek Rathore on 13/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct NewRoom: View{
    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) var dismiss
    @StateObject var viewModelAdapter: NewRoomViewModelAdapter = NewRoomViewModelAdapter()
    var body: some View{
        ZStack{
            VStack{
                RectangularTextField(
                    title: "Room Name",
                    text: Binding(get: { viewModelAdapter.roomName }, set: {value in viewModelAdapter.onValueChange(newText: value)})
                )
                .padding()
                if let error = viewModelAdapter.error{
                    ErrorText(text: error)
                }
                Spacer()
            }
            VStack{
                Spacer()
                HStack{
                    Spacer()
                    Button(action: {
                        viewModelAdapter.addRoom()
                    }){
                        Image(systemName: "checkmark.circle")
                            .font(.system(size: 24, weight: .bold))
                            .foregroundColor(.white)
                            .padding()
                            .background(Color.blue)
                            .clipShape(Circle())
                            .shadow(color: .gray, radius: 4, x: 0, y: 4)
                    }
                    .padding()
                }
            }
        }
        .task{
            await viewModelAdapter.observeStates()
        }
        .onChange(of: viewModelAdapter.navigate) { _, newValue in
            if newValue {
                navigation.navigateBack()
                viewModelAdapter.resetNavigate()
            }
        }
        .navigationBarBackButtonHidden(true)
        .navigationTitle("New Room")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar{
            ToolbarItem(placement: .topBarLeading){
                BackButton{
                    dismiss()
                    navigation.popBackStack()
                }
            }
        }
    }
}
