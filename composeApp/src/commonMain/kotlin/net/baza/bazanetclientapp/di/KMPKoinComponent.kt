package net.baza.bazanetclientapp.di

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

// не трогать
internal abstract class KMPKoinComponent : KoinComponent {
    override fun getKoin(): Koin = LibDependencyInitializer.koinApp?.koin!!
}