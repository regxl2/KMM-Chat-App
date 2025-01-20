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
    @StateObject private var navigation: Navigation = Navigation()
    @ObservedObject private var viewModel: MainViewModel = ViewModelProvider.shared.mainViewModel
    
    var body: some View {
        NavigationStack(path: $navigation.navPath){
            ZStack{
                contentView()
            }
            .navigationDestination(for: NavRoutes.self){ route in
                content(route: route)
            }
        }
        .environmentObject(navigation)
        .environmentObject(viewModel)
    }
}

extension ContentView {
    @ViewBuilder
    func contentView() -> some View {
        switch viewModel.state(\.destination, equals: { $0 == $1 }, mapper: { $0 }) {
        case .auth: content(route: NavRoutes.SignIn)
        case .conversations: content(route: NavRoutes.Conversations)
        case .loading: content(route: NavRoutes.Loading)
        default: EmptyView()
        }
    }
}
