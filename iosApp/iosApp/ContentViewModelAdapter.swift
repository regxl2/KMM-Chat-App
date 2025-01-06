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
    
    @MainActor
    func observeState() async{
        for await state in viewModel.destination{
            self.rootScreen = state
        }
    }
    
    func changeDestination( destination: Destination){
        rootScreen = destination
    }
}
