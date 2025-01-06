//
//  OtpPassVerify.swift
//  Experiment
//
//  Created by Abhishek Rathore on 11/12/24.
//

import SwiftUI

struct OtpPassVerify: View {
    @Environment(Navigation.self) private var navigation
    @StateObject private var viewModelAdapter: OtpPassVerifyViewModelAdapter = OtpPassVerifyViewModelAdapter()
    let email: String
    
    init(email: String){
        self.email = email
    }
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModelAdapter.isLoading, content: {
            OtpView(title: "Password Reset", email: email,
                    code: Binding(
                        get: {viewModelAdapter.code},
                        set: { value in viewModelAdapter.onOtpChange(code: value)}
                    ),
                    error: viewModelAdapter.error,
                    resendAction: {viewModelAdapter.resendOtp()},
                    verifyAction: {viewModelAdapter.verifyPassReset()}
            )
        })
        .navigationBarBackButtonHidden()
        .toolbar{
            ToolbarItem(placement: .navigationBarLeading){
                BackButton{
                    navigation.navigateBack()
                }
            }
        }
        .onAppear{
            viewModelAdapter.initEmail(email: email)
        }
        .task {
            await viewModelAdapter.observeState()
        }
        .onChange(of: viewModelAdapter.navigateScreen) { _, newValue in
            if newValue {
                navigation.navigateTo(
                    destination: NavRoutes.ResetPassword(email: email),
                    popUpToBuilder: {
                        navigation.navigateToStartDestination()
                    }
                )
                viewModelAdapter.resetNavigate()
            }
        }
    }
}

#Preview {
    OtpPassVerify(email: "Example@gmail.com")
        .environment(Navigation())
}
