//
//  RectangularTextField.swift
//  Experiment
//
//  Created by Abhishek Rathore on 10/12/24.
//

import SwiftUI

struct RectangularTextField: View {
    var title: String
    @Binding var text: String
    var body: some View{
        TextField(title, text: $text)
            .padding()
            .background(Color(.systemGray6))
            .cornerRadius(5)
            .autocorrectionDisabled()
            .textInputAutocapitalization(.never)
    }
}
