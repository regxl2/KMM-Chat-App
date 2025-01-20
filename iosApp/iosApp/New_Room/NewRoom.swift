//
//  NewRoom.swift
//  iosApp
//
//  Created by Abhishek Rathore on 13/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct NewRoom: View{
    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) var dismiss
    @ObservedObject var viewModel = ViewModelProvider.shared.newRoomViewModel
    var body: some View{
        ZStack{
            VStack{
                RectangularTextField(
                    title: "Room Name",
                    text: Binding(get: { viewModel.roomNameState }, set: {value in viewModel.onValueChange(newText: value)})
                )
                .padding()
                if let error = viewModel.errorState {
                    ErrorText(text: error)
                }
                Spacer()
            }
            VStack{
                Spacer()
                HStack{
                    Spacer()
                    Button(action: {
                        viewModel.addRoom()
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
        .onChange(of: viewModel.navigateState) { _, newValue in
            if newValue {
                navigation.navigateBack()
                viewModel.resetNavigate()
            }
        }
        .onDisappear{
            viewModel.resetStates()
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

extension NewRoomViewModel{
    var roomNameState: String {
        get{
            return self.state(\.roomName) ?? ""
        }
    }
    var errorState: String?{
        get{
            return self.stateNullable(\.error)
        }
    }
    var navigateState: Bool {
        get{
            return self.state(\.navigate)
        }
    }
}
