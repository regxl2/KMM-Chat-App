//
//  FloatingButton.swift
//  iosApp
//
//  Created by Abhishek Rathore on 24/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct FloatingButton: View {
    var body: some View {
        NavigationLink(destination: Text("Chat")){
            Image(systemName: "plus")
                .foregroundColor(.black)
                .padding()
                .background(.white)
                .clipShape(Circle())
                .shadow(radius: 10)
                .padding(16)
        }
    }
}

#Preview {
    FloatingButton()
}
