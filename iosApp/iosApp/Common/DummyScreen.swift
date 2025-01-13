////
////  DummyScreen.swift
////  iosApp
////
////  Created by Abhishek Rathore on 13/01/25.
////  Copyright © 2025 orgName. All rights reserved.
////
//import SwiftUI
//struct DummyScreen: View{
//    @EnvironmentObject private var navigation: Navigation
//    @Environment(\.presentationMode) var presentationMode
//    @State var randomColor: Color = .white
//    var body: some View{
//        Color(randomColor).ignoresSafeArea(.all)
//            .onAppear {
//                            randomColor = generateRandomColor()
//                        }
//            .onTapGesture{
//                navigation.navigateTo(destination: NavRoutes.Dummy)
//            }
//            .navigationBarBackButtonHidden(true)
//                    .navigationTitle("Dummy")
//                    .navigationBarTitleDisplayMode(.inline)
//                    .toolbar {
//                        ToolbarItem(placement: .topBarLeading) {
//                            BackButton {
//                                presentationMode.wrappedValue.dismiss()
//                                navigation.popBackStack()
//                            }
//                        }
//                    }
//    }
//    private func generateRandomColor() -> Color {
//            // Generate random values for red, green, and blue components
//            let red = Double.random(in: 0...1)
//            let green = Double.random(in: 0...1)
//            let blue = Double.random(in: 0...1)
//            
//            // Create a new color using the random values
//            return Color(red: red, green: green, blue: blue)
//        }
//}
