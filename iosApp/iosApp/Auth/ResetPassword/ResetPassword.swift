//
//  ResetPassword.swift
//  Experiment
//
//  Created by Abhishek Rathore on 10/12/24.
//
import SwiftUI
import Shared

struct ResetPassword: View{
    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) var dismiss
    @ObservedObject private var viewModel = ViewModelProvider.shared.resetPasswordViewModel
    
    init(email: String){
        viewModel.doInitEmail(email: email)
    }
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModel.state.isLoading){
            VStack(spacing: 16){
                LargeTitleText(title: "Reset Password")
                PasswordTextField(
                    title: "Password",
                    password: Binding(
                        get: { viewModel.state.password },
                        set: { value in viewModel.onChangePassword(password: value) }
                    )
                )
                PasswordTextField(
                    title: "Confirm Password",
                    password: Binding(
                        get: { viewModel.state.confirmPassword },
                        set: { value in viewModel.onChangeConfirmPassword(confirmPassword: value) }
                    )
                )
                if let error = viewModel.state.error{
                    ErrorText(text: error)
                }
                RectangularButton(
                    action: { viewModel.onClickSubmit() },
                    title: "Reset Password",
                    isDisabled: false
                )
                Spacer()
            }
            .padding()
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
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
        .onChange(of: viewModel.state.navigate){
            _, newValue in
            if newValue {
                navigation.navigateToStartDestination()
                viewModel.resetNavigate()
            }
        }
    }
}

extension ResetPasswordViewModel{
    var state: ResetPasswordUi{
        get{
            return self.state(
                \.resetPasswordUiState,
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
    ResetPassword(email: "example@gmail.com")
        .environmentObject(Navigation())
}
