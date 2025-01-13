//
//  ResetPassword.swift
//  Experiment
//
//  Created by Abhishek Rathore on 10/12/24.
//
import SwiftUI

struct ResetPassword: View{
    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) var dismiss
    @StateObject private var viewModelAdapter: ResetPasswordViewModelAdapter = ResetPasswordViewModelAdapter()
    let email: String
    
    init(email: String){
        self.email = email
    }
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModelAdapter.isLoading){
            VStack(spacing: 16){
                LargeTitleText(title: "Reset Password")
                PasswordTextField(
                    title: "Password",
                    password: Binding(
                        get: {viewModelAdapter.password},
                        set: {value in viewModelAdapter.onChangePassword(password: value)}
                    )
                )
                PasswordTextField(
                    title: "Confirm Password",
                    password: Binding(
                        get: {viewModelAdapter.confirmPassword},
                        set: {value in viewModelAdapter.onChangeConfirmPassword(confirmPassword: value)}
                    )
                )
                if let error = viewModelAdapter.error{
                    ErrorText(text: error)
                }
                RectangularButton(action: {viewModelAdapter.onClickResetPassword()}, title: "Reset Password", isDisabled: false)
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
        .onAppear{
            viewModelAdapter.initEmail(email: email)
        }
        .task {
            await viewModelAdapter.observeState()
        }
        .onChange(of: viewModelAdapter.navigate){
            _, newValue in
            if newValue {
                navigation.navigateToStartDestination()
                viewModelAdapter.resetNavigate()
            }
        }
    }
}

#Preview {
    ResetPassword(email: "example@gmail.com")
        .environmentObject(Navigation())
}
