//
//  LargeTitleText.swift
//  Experiment
//
//  Created by Abhishek Rathore on 11/12/24.
//

import SwiftUI

struct LargeTitleText: View{
    let title: String
    var body: some View{
        Text(title)
            .font(.largeTitle)
            .fontWeight(Font.Weight.bold)
    }
}
