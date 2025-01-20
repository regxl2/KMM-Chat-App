//
//  OtpPassVerify.swift
//  Experiment
//
//  Created by Abhishek Rathore on 11/12/24.
//

import SwiftUI
import Shared

struct OtpPassVerify: View {
    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) private var dismiss
    @ObservedObject private var viewModel = ViewModelProvider.shared.otpPasswordViewModel
    
    init(email: String){
        viewModel.doInitEmail(email: email)
    }
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModel.state.isLoading, content: {
            OtpView(title: "Password Reset",
                    email: viewModel.getEmail(),
                    error: viewModel.state.error,
                    onOtpChange: {value in viewModel.onOtpChange(otp: value)},
                    resendAction: {viewModel.resendOtp()},
                    verifyAction: {viewModel.verifyPassReset()}
            )
        })
        .navigationBarBackButtonHidden()
        .toolbar {
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
        .onChange(of: viewModel.state.navigateScreen) { _, newValue in
            if newValue {
                navigation.navigateTo(
                    destination: NavRoutes.ResetPassword(email: viewModel.getEmail()),
                    popUpToBuilder: {
                        navigation.navigateToStartDestination()
                    }
                )
                viewModel.resetNavigate()
            }
        }
    }
}

extension OtpPassVerifyViewModel{
    var state: OtpUi{
        get{
            return self.state(
                \.otpPassVerifyUiState,
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
    OtpPassVerify(email: "Example@gmail.com")
        .environmentObject(Navigation())
}
