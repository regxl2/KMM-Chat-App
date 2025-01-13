//
//  iOSViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 22/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared

class ContentViewModelAdapter: ObservableObject{
    private let viewModel: MainViewModel = ViewModelProvider.shared.mainViewModel
    @Published var rootScreen: Destination = .loading
    @Published var userId: String = ""
    
    @MainActor
    func observeState() async{
        await withTaskGroup(of: Void.self) { group in
            group.addTask { [weak self] in
                guard let self = self else {return}
                for await state in self.viewModel.destination{
                    await MainActor.run{
                        self.rootScreen = state
                    }
                }
            }
            group.addTask { [weak self] in
                guard let self = self else {return}
                for await userId in self.viewModel.userId{
                    await MainActor.run{
                        self.userId = userId
                    }
                }
            }
        }
    }
    
    func changeDestination( destination: Destination){
        rootScreen = destination
    }
    
    deinit{
        print("main ViewModel")
    }
}
