package com.example.encryptiondatastore.data.di.qualifiers

import javax.inject.Qualifier

enum class EdsDispatchers { IO, Default }

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: EdsDispatchers)

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope
