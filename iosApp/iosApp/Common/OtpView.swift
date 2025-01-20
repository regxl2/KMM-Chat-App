//
//  Otp.swift
//  Experiment
//
//  Created by Abhishek Rathore on 11/12/24.
//

import SwiftUI

struct OtpView: View {
    let title: String
    let email: String
    let error: String?
    @FocusState private var focus: Int?
    let onOtpChange: (String) -> Void
    let resendAction: ()-> Void
    let verifyAction: ()-> Void
    
    @State var code = Array(repeating: "", count: 4)
    
    private func isbuttonDisabled() -> Bool{
        code[0].isEmpty || code[1].isEmpty || code[2].isEmpty || code[3].isEmpty
    }
    
    var body: some View{
        VStack(spacing: 16){
            VStack(spacing: 8){
                LargeTitleText(title: title)
                Text("Enter the OTP sent to **\(email)**").multilineTextAlignment(.center)
            }
            HStack{
                ForEach(0..<4, id: \.self){ index in
                    TextField("", text: $code[index])
                        .multilineTextAlignment(.center)
                        .keyboardType(.numberPad)
                        .frame(width: 64, height: 64)
                        .background(Color(.systemGray6))
                        .cornerRadius(5)
                        .focused($focus, equals: index)
                        .tag(index)
                        .onChange(of: code[index]){ oldValue, newValue in
                            if !newValue.isEmpty{
                                if(newValue.count > 1){
                                    if (oldValue.count == 0 || oldValue.first == newValue.first){
                                        code[index] = String(newValue.suffix(1))
                                    }
                                    else{
                                        code[index] = String(newValue.prefix(1))
                                    }
                                }
                                if index < 4 { focus = index + 1 }
                                else { focus = nil }
                            }
                            else{
                                if index > 0 { focus = index - 1 }
                            }
                            onOtpChange(code.joined())
                        }
                        .onKeyPress(.delete){
                            if(code[index].isEmpty){
                                if index > 0 {focus = index - 1}
                                return .handled
                            }
                            return .ignored
                        }
                }
            }
            if error != nil {
                ErrorText(text: error!)
            }
            HStack{
                Text("Haven't received the OTP?")
                Button("Resend OTP", action: resendAction )
            }
            RectangularButton(action: verifyAction, title: "Verify", isDisabled: isbuttonDisabled())
            Spacer()
        }
        .onAppear{
            focus = 0
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}


#Preview {
    @Previewable @State var code: [String] = Array(repeating: "", count: 4)
    OtpView(title: "Account Verification", email: "example.com", error: nil,
            onOtpChange: {_ in},
            resendAction: {}, verifyAction: {})
}

