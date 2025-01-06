//
//  CircularIndicatorBox.swift
//  iosApp
//
//  Created by Abhishek Rathore on 18/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct CircularIndicatorBox<Content: View>: View{
    let isLoading: Bool
    let content: Content
    
    init(isLoading: Bool, content: ()-> Content) {
        self.isLoading = isLoading
        self.content = content()
    }
    
    var body: some View{
        ZStack{
            content
            if(isLoading){
                VStack{
                    ProgressView()
                        .frame(width: 64, height: 64)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.white.opacity(0.5))
            }
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
    
}
