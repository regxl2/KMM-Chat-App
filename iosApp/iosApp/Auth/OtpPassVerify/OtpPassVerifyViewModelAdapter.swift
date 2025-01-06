//
//  OtpPassVerifyViewModelAdapter.swift
//  iosApp
//
//  Created by Abhishek Rathore on 22/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared


class OtpPassVerifyViewModelAdapter: ObservableObject{
    private let otpPassVerifyViewModel: OtpPassVerifyViewModel = ViewModelProvider.shared.otpPasswordViewModel
    @Published var code: [String] = Array(repeating: "", count: 4)
    @Published var navigateScreen: Bool = false
    @Published var isLoading: Bool = false
    @Published var error: String? = nil
    
    func initEmail(email: String){
        otpPassVerifyViewModel.doInitEmail(email: email)
    }
    
    @MainActor
    func observeState() async{
        for await state in otpPassVerifyViewModel.otpPassVerifyUiState{
            self.navigateScreen = state.navigateScreen
            self.isLoading = state.isLoading
            self.error = state.error
        }
    }
    
    func onOtpChange(code: [String]){
        self.code = code
        otpPassVerifyViewModel.onOtpChange(otp: code.joined())
    }
    
    func resendOtp(){
        otpPassVerifyViewModel.resendOtp()
    }
    
    func verifyPassReset(){
        otpPassVerifyViewModel.verifyPassReset()
    }
    
    func resetNavigate(){
        otpPassVerifyViewModel.resetNavigate()
    }
    
    deinit{
        onOtpChange(code: Array(repeating: "", count: 4))
    }
}
