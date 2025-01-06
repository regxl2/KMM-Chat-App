//
//  ForgotPassword.swift
//  Experiment
//
//  Created by Abhishek Rathore on 10/12/24.
//
import SwiftUI

struct ForgotPassword: View{
    @StateObject private var viewModelAdapter: ForgotPasswordViewModelAdapter = ForgotPasswordViewModelAdapter()
    @Environment(Navigation.self) var navigation
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModelAdapter.isLoading, content: {
            VStack(spacing: 16){
                LargeTitleText(title: "Forgot Password")
                RectangularTextField(
                    title: "Email",
                    text: Binding(
                        get: {viewModelAdapter.email},
                        set: {value in viewModelAdapter.onEmailChange(email: value)}
                    )
                )
                RectangularButton(
                    action: {
                        viewModelAdapter.onSubmit()
                    }
                    ,
                    title: "Submit", isDisabled: viewModelAdapter.email.isEmpty
                )
                if let error = viewModelAdapter.error {
                    ErrorText(text: error)
                }
                Button("Back to Sign In", action: {navigation.navigateBack()})
                Spacer()
            }
        })
        .task {
            await viewModelAdapter.observeState()
        }
        .onChange(of: viewModelAdapter.navigateToOtp,{ _, newValue in
            if newValue {
                navigation.navigateTo(
                    destination: NavRoutes.OtpPassVerify(email: viewModelAdapter.email)
                )
                viewModelAdapter.resetNavigate()
            }
        })
        .navigationBarBackButtonHidden()
        .toolbar{
            ToolbarItem(placement: .navigationBarLeading){
                BackButton{
                    navigation.navigateBack()
                }
            }
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}


#Preview {
    ForgotPassword()
        .environment(Navigation())
}
