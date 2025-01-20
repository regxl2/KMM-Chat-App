//
//  OtpAccountVerify.swift
//  Experiment
//
//  Created by Abhishek Rathore on 11/12/24.
//
import SwiftUI
import Shared

struct OtpAccountVerify: View{
    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) var dismiss
    @ObservedObject private var viewModel = ViewModelProvider.shared.otpAccountVerifyViewModel
    
    
    init(email: String) {
        viewModel.doInitEmail(email: email)
    }
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModel.state.isLoading, content: {
            OtpView(title: "Account Verification",
                    email: viewModel.getEmail(),
                    error: viewModel.state.error,
                    onOtpChange: {value in viewModel.onOtpChange(otp: value)},
                    resendAction: { viewModel.resendOtp() },
                    verifyAction: { viewModel.verifyAccount() }
            )
        })
        .navigationBarBackButtonHidden()
        .toolbar{
            ToolbarItem(placement: .navigationBarLeading){
                BackButton{
                    dismiss()
                    navigation.popBackStack()
                }
            }
        }
        .onDisappear{
            viewModel.resetState()
        }
        .onChange(of: viewModel.state.navigateScreen ) { _, newValue in
            if newValue {
                navigation.navigateToStartDestination()
                viewModel.resetNavigate()
            }
        }
    }
}

extension OtpAccountVerifyViewModel {
    var state: OtpUi {
        get{
            return self.state(
                \.otpUiState,
                 equals: { $0 == $1 },
                 mapper: { $0 }
            )
        }
    }
    
    func resetState(){
        resetNavigate()
    }
}

#Preview {
    OtpAccountVerify(email: "example@gmail.com")
        .environmentObject(Navigation())
}
