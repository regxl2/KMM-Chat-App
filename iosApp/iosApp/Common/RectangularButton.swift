//
//  Button.swift
//  Experiment
//
//  Created by Abhishek Rathore on 10/12/24.
//
import SwiftUI

struct RectangularButton: View {
    let action: () -> Void
    let title: String
    let isDisabled: Bool
    var body: some View {
        Button(action: action, label: {
          HStack {
            Spacer()
              Text(title)
               .foregroundColor(.white)
            Spacer()
            }.padding()
                .background(isDisabled ? Color.gray : Color.blue )
             .cornerRadius(5.0)
        })
        .disabled(isDisabled)
    }
}
