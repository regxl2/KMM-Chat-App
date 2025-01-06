//
//  ResetPasswordViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 22/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

class ResetPasswordViewModelAdapter: ObservableObject {
    private let viewModel: ResetPasswordViewModel = ViewModelProvider.shared.resetPasswordViewModel
    
    @Published var password: String = ""
    @Published var confirmPassword: String = ""
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var navigate: Bool = false
    
    @MainActor
    func observeState() async {
        for await state in viewModel.resetPasswordUiState{
            self.password = state.password
            self.confirmPassword = state.confirmPassword
            self.isLoading = state.isLoading
            self.error = state.error
            self.navigate = state.navigate
        }
    }
    
    func initEmail(email: String){
        viewModel.doInitEmail(email: email)
    }
    
    func onChangePassword(password: String){
        viewModel.onChangePassword(password: password)
    }
    
    func onChangeConfirmPassword(confirmPassword: String){
        viewModel.onChangeConfirmPassword(confirmPassword: confirmPassword)
    }
    
    func onClickResetPassword(){
        viewModel.onClickSubmit()
    }
    
    func resetNavigate(){
        viewModel.resetNavigate()
    }
    
    deinit{
        onChangePassword(password: "")
        onChangeConfirmPassword(confirmPassword: "")
    }
}
