//
//  AvatarAlt.swift
//  iosApp
//
//  Created by Abhishek Rathore on 12/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct AvatarAlt: View {
    let text: String
    var body: some View{
        Text(text)
            .foregroundStyle(.white)
            .background(Circle().foregroundColor(Color.gray).frame(width: 50, height: 50))
            .frame(width: 50, height: 50)
    }
}

