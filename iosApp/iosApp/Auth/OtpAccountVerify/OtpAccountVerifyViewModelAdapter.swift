//
//  OtpAccountVerifyViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 21/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared

class OtpAccountVerifyViewModelAdapter: ObservableObject{
    let otpAccountVerifyViewModel: OtpAccountVerifyViewModel = ViewModelProvider.shared.otpAccountVerifyViewModel
    @Published var code: [String] = Array(repeating: "", count: 4)
    @Published var navigateScreen: Bool = false
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    
    func initEmail(email: String){
        otpAccountVerifyViewModel.doInitEmail(email: email)
    }
    
    @MainActor
    func observeState() async{
        for await state in otpAccountVerifyViewModel.otpUiState{
            self.navigateScreen = state.navigateScreen
            self.isLoading = state.isLoading
            self.error = state.error
        }
    }
    
    func onOtpChange(code: [String]){
        self.code = code
        otpAccountVerifyViewModel.onOtpChange(otp: code.joined())
    }
    
    func resendOtp(){
        otpAccountVerifyViewModel.resendOtp()
    }
    
    func verifyAccount(){
        otpAccountVerifyViewModel.verifyAccount()
    }
    
    func resetNavigate(){
        otpAccountVerifyViewModel.resetNavigate()
    }
    
    deinit{
        onOtpChange(code: Array(repeating: "", count: 4))
    }
    
}
