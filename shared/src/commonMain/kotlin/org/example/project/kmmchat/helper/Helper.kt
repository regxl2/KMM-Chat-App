package org.example.project.kmmchat.helper

import org.example.project.kmmchat.di.getSharedModule
import org.example.project.kmmchat.di.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null){
    startKoin {
        config?.invoke(this)
        modules(getSharedModule() + platformModule)
    }
}

