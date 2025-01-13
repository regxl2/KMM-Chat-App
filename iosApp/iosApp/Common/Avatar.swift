//
//  Avatar.swift
//  iosApp
//
//  Created by Abhishek Rathore on 12/01/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct Avatar: View{
    let url: String
    var body: some View{
        AsyncImage(url: URL(string: url )){phase in
            if let image = phase.image{
                image
            }
            else{
                Color.gray
            }
        }
            .frame(width: 50, height: 50)
            .clipShape(Circle())
            .padding(8)
    }
}
