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
    @StateObject private var otpAccountVerifyAdapter: OtpAccountVerifyViewModelAdapter = OtpAccountVerifyViewModelAdapter()
    let email: String
    
    
    init(email: String) {
        self.email = email
    }
    
    var body: some View{
        CircularIndicatorBox(isLoading: otpAccountVerifyAdapter.isLoading, content: {
            OtpView(title: "Account Verification",
                    email: email,
                    code: Binding(
                        get: {otpAccountVerifyAdapter.code},
                        set: { value in otpAccountVerifyAdapter.onOtpChange(code: value)}
                    ),
                    error: otpAccountVerifyAdapter.error,
                    resendAction: {otpAccountVerifyAdapter.resendOtp()},
                    verifyAction: {otpAccountVerifyAdapter.verifyAccount()}
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
        .onAppear{
            otpAccountVerifyAdapter.initEmail(email: email)
        }
        .task {
            await otpAccountVerifyAdapter.observeState()
        }
        .onChange(of: otpAccountVerifyAdapter.navigateScreen) { _, newValue in
            if newValue {
                navigation.navigateToStartDestination()
                otpAccountVerifyAdapter.resetNavigate()
            }
        }
    }
    
}

#Preview {
    OtpAccountVerify(email: "example@gmail.com")
        .environmentObject(Navigation())
}
