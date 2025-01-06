//
//  SignUpViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 17/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared


@Observable
class SignUpViewModelAdapter{
    private let signUpViewModel: SignUpViewModel = ViewModelProvider.shared.signUpViewModel
    
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var isLoading: Bool = false
    var error: String? = nil
    var navigateToOtp: Bool = false
    var isSignUpButtonEnabled: Bool = false
    
    @MainActor
    func observeState() async{
        for await state in signUpViewModel.signUpUiState {
            self.name = state.name
            self.email = state.email
            self.password = state.password
            self.isLoading = state.isLoading
            self.error = state.error
            self.navigateToOtp = state.navigateToOtp
            self.isSignUpButtonEnabled = !state.isLoading &&
                                         !state.name.isEmpty &&
                                         !state.email.isEmpty &&
                                         !state.password.isEmpty
        }
    }
    
    func signUp() {
        signUpViewModel.signUp()
    }
    
    func onNameChange(name: String) {
        signUpViewModel.onNameChange(name: name)
    }
    
    func onEmailChange(email: String) {
        signUpViewModel.onEmailChange(email: email)
    }
    
    func onPasswordChange(password: String) {
        signUpViewModel.onPasswordChange(password: password)
    }
    
    func resetNavigate() {
        signUpViewModel.resetNavigate()
    }
    
    deinit{
        onNameChange(name: "")
        onEmailChange(email: "")
        onPasswordChange(password: "")
    }
    
}
