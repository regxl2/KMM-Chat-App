//
//  BackButton.swift
//  iosApp
//
//  Created by Abhishek Rathore on 06/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct BackButton: View {
    @Environment(\.presentationMode) private var presentationMode
    let action: () -> Void
    
    var body: some View {
        Button(action: {
            action()
            presentationMode.wrappedValue.dismiss()
        }) {
            Image(systemName: "arrow.backward")
        }
    }
}
