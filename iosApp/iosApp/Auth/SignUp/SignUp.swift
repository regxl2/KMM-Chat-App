//
//  SignUp.swift
//  Experiment
//
//  Created by Abhishek Rathore on 10/12/24.
//

import SwiftUI
import Shared

struct SignUp: View{
    @ObservedObject private var viewModel = ViewModelProvider.shared.signUpViewModel
    @EnvironmentObject private var navigation: Navigation
    @Environment(\.dismiss) var dismiss
    
    var body: some View{
        CircularIndicatorBox(isLoading: viewModel.state.isLoading, content: {
            VStack(spacing: 16){
                VStack(spacing: 8){
                    LargeTitleText(title: "Create Your Account")
                    Text("Let's sign up and create your account and get started")
                        .multilineTextAlignment(.center)
                }
                RectangularTextField(title: "Name",text: Binding(
                    get: {viewModel.state.name},
                    set: {viewModel.onNameChange(name: $0)})
                )
                RectangularTextField(title: "Email", text: Binding(
                    get: {viewModel.state.email},
                    set:{viewModel.onEmailChange(email: $0)})
                )
                PasswordTextField(title: "Password", password: Binding(
                    get: {viewModel.state.password},
                    set: {viewModel.onPasswordChange(password: $0)})
                )
                if let error = viewModel.state.error{
                    ErrorText(text: error)
                }
                RectangularButton(action: {viewModel.signUp()}, title: "Sign Up", isDisabled: !viewModel.state.isSignUpButtonEnabled )
                HStack{
                    Text("Already have an account?")
                    Button("Sign In", action: {
                        navigation.navigateBack()
                    })
                }
                Spacer()
            }
        })
        .navigationBarBackButtonHidden()
        .toolbar{
            ToolbarItem(placement: .navigationBarLeading){
                BackButton{
                    dismiss()
                    navigation.navigateBack()
                }
            }
        }
        .onDisappear{
            viewModel.resetStates()
        }
        .onChange(of: viewModel.state.navigateToOtp){ _, newState in
            if(newState){
                navigation.navigateTo(destination: NavRoutes.OtpAccountVerify(email: viewModel.state.email))
                viewModel.resetNavigate()
            }
        }
    }
}

extension SignUpViewModel {
    var state: SignUpUi{
        get{
            return self.state(
                \.signUpUiState,
                 equals: { $0 == $1 },
                 mapper: { $0 }
            )
        }
    }
    
    func resetStates(){
        self.onNameChange(name: "")
        self.onEmailChange(email: "")
        self.onPasswordChange(password:"")
        self.resetNavigate()
    }
}

#Preview {
    SignUp()
        .environmentObject(Navigation())
}
