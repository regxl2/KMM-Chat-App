//
//  NewRoomViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 13/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

class NewRoomViewModelAdapter: ObservableObject{
    private let viewModel : NewRoomViewModel = ViewModelProvider.shared.newRoomViewModel
    @Published var roomName: String = ""
    @Published var error : String? = nil
    @Published var navigate: Bool = false
    
    func observeStates() async {
        await withTaskGroup(of: Void.self){ group in
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await roomName in self.viewModel.roomName{
                    await MainActor.run{
                        self.roomName = roomName
                    }
                }
            }
            
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await error in self.viewModel.error{
                    await MainActor.run{
                        self.error = error
                    }
                }
            }
            
            group.addTask { [weak self] in
                guard let self = self else { return }
                for await navigate in self.viewModel.navigate{
                    await MainActor.run{
                        self.navigate = navigate.boolValue
                    }
                }
            }
        }
    }
    
    func onValueChange(newText: String){
        viewModel.onValueChange(newText: newText)
    }
    
    func resetNavigate(){
        viewModel.resetNavigate()
    }
    
    func addRoom(){
        viewModel.addRoom()
    }
    
    deinit{
        viewModel.clearStates()
        print("deinit New Room")
    }
}
