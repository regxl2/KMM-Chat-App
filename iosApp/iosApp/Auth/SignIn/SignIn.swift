//
//  SignIn.swift
//  iosApp
//
//  Created by Abhishek Rathore on 09/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI

struct SignIn: View{
    @StateObject private var viewModelAdapter: SignInViewModelAdpater = SignInViewModelAdpater()
    @Environment(Navigation.self) private var navigation
    @EnvironmentObject private var iOSViewModelAdapter: ContentViewModelAdapter
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModelAdapter.isLoading){
            VStack( spacing: 16){
                VStack(alignment: .leading, spacing: 16){
                    LargeTitleText(title: "Sign In to your account")
                    Text("Let's sign in to your account and get started")
                        .multilineTextAlignment(.center)
                }
                
                RectangularTextField(
                    title: "Email",
                    text: Binding(
                        get: {viewModelAdapter.email},
                        set: {value in viewModelAdapter.onEmailChange(email: value)}
                    )
                )
                
                PasswordTextField(
                    title: "Password",
                    password: Binding(
                        get: {viewModelAdapter.password},
                        set: {value in viewModelAdapter.onPasswordChange(password: value)}
                    )
                )
                if let error = viewModelAdapter.error{
                    ErrorText(text: error)
                }
                RectangularButton(action: {viewModelAdapter.signIn()}, title: "Sign In", isDisabled: viewModelAdapter.isButtonDisabled)
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
        .task {
            await viewModelAdapter.observeState()
        }
        .onChange(of: viewModelAdapter.navigate) { _, newValue in
            if newValue {
                navigation.navigateToStartDestination()
                iOSViewModelAdapter.changeDestination(destination: .conversations)
                viewModelAdapter.resetNavigate()
            }
        }
    }
}


#Preview{
    SignIn()
        .environment(Navigation())
}

