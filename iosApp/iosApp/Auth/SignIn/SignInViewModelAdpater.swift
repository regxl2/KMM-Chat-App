//
//  SignInViewModelAdpater.swift
//  iosApp
//
//  Created by Abhishek Rathore on 22/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared


class SignInViewModelAdpater: ObservableObject{
    let viewModel: SignInViewModel = ViewModelProvider.shared.signInViewModel
    @Published var email: String = ""
    @Published var password: String = ""
    @Published var navigate: Bool = false
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    @Published var isButtonDisabled: Bool = false
    
    init(){
        print("sigin viewmodel")
    }
    
    @MainActor
    func observeState() async{
        for await state in viewModel.signInUiState{
            self.email = state.email
            self.password = state.password
            self.navigate = state.navigate
            self.isLoading = state.isLoading
            self.error = state.error
            self.isButtonDisabled = state.email.isEmpty || state.password.isEmpty
        }
    }
    
    func signIn(){
        viewModel.signIn()
    }
    
    func resetNavigate(){
        viewModel.resetNavigate()
    }
    
    func onEmailChange(email: String){
        viewModel.onEmailChange(email: email)
    }
    
    func onPasswordChange(password: String){
        viewModel.onPasswordChange(password: password)
    }
    
    deinit{
        print("sign in viewmodel")
        onEmailChange(email: "")
        onPasswordChange(password: "")
    }
}
