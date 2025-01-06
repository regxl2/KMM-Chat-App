//
//  ContentView.swift
//  iosApp
//
//  Created by Abhishek Rathore on 22/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct ContentView: View {
    @State private var navigation: Navigation = Navigation()
    @StateObject private var viewModelAdapter: ContentViewModelAdapter = ContentViewModelAdapter()
    
    var body: some View {
        NavigationStack(path: $navigation.navPath){
            Group{
                switch viewModelAdapter.rootScreen {
                case .auth: content(route: NavRoutes.SignIn)
                case .conversations: content(route: NavRoutes.Conversations)
                case .loading: content(route: NavRoutes.Loading)
                }
            }
            .navigationDestination(for: NavRoutes.self){ route in
                content(route: route)
            }
        }
        .task {
            await viewModelAdapter.observeState()
        }
        .environment(navigation)
        .environmentObject(viewModelAdapter)
    }
}
