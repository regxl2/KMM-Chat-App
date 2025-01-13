//
//  LoadingScreen.swift
//  iosApp
//
//  Created by Abhishek Rathore on 12/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct LoadingScreen: View {
    var body: some View{
        ZStack{
            Color(.systemBackground)
                .ignoresSafeArea(.all)
            ProgressView().progressViewStyle(.circular)
        }
    }
}
