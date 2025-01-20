//
//  ForgotPassword.swift
//  Experiment
//
//  Created by Abhishek Rathore on 10/12/24.
//
import SwiftUI
import Shared

struct ForgotPassword: View{
    @StateObject private var viewModel = ViewModelProvider.shared.forgotPasswordViewModel
    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) var dismiss
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModel.state.isLoading , content: {
            VStack(spacing: 16){
                RectangularTextField(
                    title: "Email",
                    text: Binding(
                        get: { viewModel.state.email },
                        set: {value in viewModel.onEmailChange(email: value)}
                    )
                )
                RectangularButton(
                    action: {
                        viewModel.onSubmit()
                    }
                    ,
                    title: "Submit", isDisabled: viewModel.state.email.isEmpty
                )
                if let error = viewModel.state.error {
                    ErrorText(text: error)
                }
                Button("Back to Sign In", action: {navigation.navigateBack()})
                Spacer()
            }
        })
        .onDisappear{
            viewModel.resetState()
        }
        .onChange(of: viewModel.state.navigateToOtp){ _ , newValue in
            if newValue {
                navigation.navigateTo(
                    destination: NavRoutes.OtpPassVerify(email: viewModel.state.email)
                )
                viewModel.resetNavigate()
            }
        }
        .navigationTitle("Forgot Password")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden()
        .toolbar{
            ToolbarItem(placement: .navigationBarLeading){
                BackButton{
                    dismiss()
                    navigation.popBackStack()                }
            }
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

extension ForgotPasswordViewModel{
    var state : ForgotPasswordUI {
        get{
            return self.state(
                \.forgotPasswordUiState,
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
    ForgotPassword()
        .environmentObject(Navigation())
}
