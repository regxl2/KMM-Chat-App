//
//  PasswordTextField.swift
//  Experiment
//
//  Created by Abhishek Rathore on 09/12/24.
//
import SwiftUI

struct PasswordTextField: View{
    let title: String
    @Binding var password: String
    @State var showPassword: Bool = false
    
    var body: some View{
        HStack{
            Group{
                if(showPassword){
                    TextField(title, text: $password)
                }
                else{
                    SecureField(title, text: $password)
                }
            }
            .autocorrectionDisabled()
            .textInputAutocapitalization(.never)
            Image(systemName: showPassword ? "eye.slash" : "eye")
                .onTapGesture {
                    showPassword.toggle()
                }
        }
        .padding()
          .background(Color(.systemGray6))
          .cornerRadius(5.0)
    }
}

#Preview {
    @Previewable @State var password: String = ""
    @Previewable @State var showPassword: Bool = false
    PasswordTextField(title: "Password", password: $password)
}
