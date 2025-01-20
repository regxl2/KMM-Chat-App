//
//  SignIn.swift
//  iosApp
//
//  Created by Abhishek Rathore on 09/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct SignIn: View{
    @ObservedObject private var viewModel = ViewModelProvider.shared.signInViewModel
    @EnvironmentObject private var navigation: Navigation
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModel.state.isLoading){
            VStack( spacing: 16){
                VStack(alignment: .leading, spacing: 16){
                    LargeTitleText(title: "Sign In to your account")
                    Text("Let's sign in to your account and get started")
                        .multilineTextAlignment(.center)
                }
                
                RectangularTextField(
                    title: "Email",
                    text: Binding(
                        get: {viewModel.state.email},
                        set: {value in viewModel.onEmailChange(email: value)}
                    )
                )
                
                PasswordTextField(
                    title: "Password",
                    password: Binding(
                        get: {viewModel.state.password},
                        set: {value in viewModel.onPasswordChange(password: value)}
                    )
                )
                if let error = viewModel.state.error{
                    ErrorText(text: error)
                }
                RectangularButton(action: {viewModel.signIn()}, title: "Sign In", isDisabled: !viewModel.state.isSignInButtonEnabled)
                VStack(spacing: 16){
                    HStack{
                        Text("Don't have an account?")
                        Button("Sign Up", action: { navigation.navigateTo(destination: NavRoutes.SignUp) })
                    }
                    Button("Forgot Password?", action: { navigation.navigateTo(destination: NavRoutes.ForgotPassword )})
                }
                Spacer()
            }
        }
        .onChange(of: viewModel.state.navigate) { _, newValue in
            if newValue {
                navigation.navigateToStartDestination()
                viewModel.resetNavigate()
            }
        }
    }
}

extension SignInViewModel{
    var state: SignInUi {
        get{
            return self.state(
                \.signInUiState,
                 equals: { $0 == $1 },
                 mapper: { $0 }
            )
        }
    }
}


#Preview{
    SignIn()
        .environmentObject(Navigation())
}

