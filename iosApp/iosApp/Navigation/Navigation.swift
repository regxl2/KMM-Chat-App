//
//  Navigation.swift
//  iosApp
//
//  Created by Abhishek Rathore on 09/12/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI

@Observable
final class Navigation{
    var navPath: NavigationPath = NavigationPath()
    var navStack: [NavRoutes] = []
    
    func navigateTo(destination: NavRoutes){
        navPath.append(destination)
        navStack.append(destination)
    }
    
    func navigateBack(){
        navPath.removeLast()
        navStack.removeLast()
    }
    
    func navigateBack(to destination: NavRoutes){
        while(!navStack.isEmpty && navStack.last != destination){
            navPath.removeLast()
            navStack.removeLast()
        }
    }
    
    func navigateTo(destination: NavRoutes, popUpToBuilder: ()-> Void){
        popUpToBuilder()
        navigateTo(destination: destination)
    }
    
    func navigateToStartDestination(){
        navPath.removeLast(navPath.count)
        navStack.removeLast(navStack.count)
    }
}
