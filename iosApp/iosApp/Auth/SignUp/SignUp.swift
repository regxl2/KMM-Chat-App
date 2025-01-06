//
//  SignUp.swift
//  Experiment
//
//  Created by Abhishek Rathore on 10/12/24.
//

import SwiftUI


struct SignUp: View{
    private let signUpViewModelAdapter: SignUpViewModelAdapter = SignUpViewModelAdapter()
    @Environment(Navigation.self) private var navigation
    var body: some View{
        CircularIndicatorBox(isLoading: signUpViewModelAdapter.isLoading, content: {
            VStack(spacing: 16){
                VStack(spacing: 8){
                    LargeTitleText(title: "Create Your Account")
                    Text("Let's sign up and create your account and get started")
                        .multilineTextAlignment(.center)
                }
                RectangularTextField(title: "Name",text: Binding(
                    get: {signUpViewModelAdapter.name},
                    set: {signUpViewModelAdapter.onNameChange(name: $0)})
                )
                RectangularTextField(title: "Email", text: Binding(
                    get: {signUpViewModelAdapter.email},
                    set:{signUpViewModelAdapter.onEmailChange(email: $0)})
                )
                PasswordTextField(title: "Password", password: Binding(
                    get: {signUpViewModelAdapter.password},
                    set: {signUpViewModelAdapter.onPasswordChange(password: $0)})
                )
                if let error = signUpViewModelAdapter.error{
                    ErrorText(text: error)
                }
                RectangularButton(action: {signUpViewModelAdapter.signUp()}, title: "Sign Up", isDisabled: !signUpViewModelAdapter.isSignUpButtonEnabled)
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
                    navigation.navigateBack()
                }
            }
        }
        .task{
            await signUpViewModelAdapter.observeState()
        }
        .onChange(of: signUpViewModelAdapter.navigateToOtp){ _, newState in
            if(newState){
                navigation.navigateTo(destination: NavRoutes.OtpAccountVerify(email: signUpViewModelAdapter.email))
                signUpViewModelAdapter.resetNavigate()
            }
        }
    }
}

#Preview {
    SignUp()
        .environment(Navigation())
}
