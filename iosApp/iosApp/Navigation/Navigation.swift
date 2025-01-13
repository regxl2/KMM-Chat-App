//
//  Navigation.swift
//  iosApp
//
//  Created by Abhishek Rathore on 09/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI

final class Navigation: ObservableObject{
    @Published var navPath: NavigationPath = NavigationPath()
    @Published var navStack: [NavRoutes] = []
    
    func navigateTo(destination: NavRoutes){
        print("\(navPath.count) \(navStack.count)")
        navPath.append(destination)
        navStack.append(destination)
        print("\(navPath.count) \(navStack.count)")
    }
    
    func navigateBack(){
        print("\(navPath.count) \(navStack.count)")
        navPath.removeLast()
        navStack.removeLast()
        print("\(navPath.count) \(navStack.count)")
    }
    
    func popBackStack(){
        print("\(navPath.count) \(navStack.count)")
        navStack.removeLast()
        print("\(navPath.count) \(navStack.count)")
    }
    
    func navigateBack(to destination: NavRoutes){
        while(!navStack.isEmpty && navStack.last != destination){
            navPath.removeLast()
            navStack.removeLast()
        }
    }
    
    func navigateTo(destination: NavRoutes, popUpToBuilder: ()-> Void){
        print("\(navPath.count) \(navStack.count)")
        popUpToBuilder()
        navigateTo(destination: destination)
        print("\(navPath.count) \(navStack.count)")
    }
    
    func navigateToStartDestination(){
        navPath.removeLast(navPath.count)
        navStack.removeLast(navStack.count)
    }
}
