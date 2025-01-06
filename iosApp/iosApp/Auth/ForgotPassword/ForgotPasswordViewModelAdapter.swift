//
//  ForgotPasswordViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 21/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared

class ForgotPasswordViewModelAdapter: ObservableObject{
    private let viewModel: ForgotPasswordViewModel = ViewModelProvider.shared.forgotPasswordViewModel
    @Published var email: String = ""
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var navigateToOtp: Bool = false
    
    @MainActor
    func observeState() async {
        for await state in viewModel.forgotPasswordUiState{
            self.email = state.email
            self.isLoading = state.isLoading
            self.error = state.error
            self.navigateToOtp = state.navigateToOtp
        }
    }
    
    func onSubmit(){
        viewModel.onSubmit()
    }
    
    func onEmailChange(email: String){
        viewModel.onEmailChange(email: email)
    }
    
    func resetNavigate(){
        viewModel.resetNavigate()
    }
    
    deinit{
        viewModel.onEmailChange(email: "")
    }
}
