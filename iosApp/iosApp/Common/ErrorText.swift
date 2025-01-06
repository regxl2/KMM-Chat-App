//
//  ErrortTextField.swift
//  iosApp
//
//  Created by Abhishek Rathore on 18/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI

struct ErrorText: View{
    let text: String
    
    var body: some View{
        Text(text)
            .foregroundColor(Color.red)
            .font(.footnote)
            .padding()
    }
}
