/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.ad

/**
 * Created by abertschi on 13.08.17.
 */
interface AdObservable {

    fun addObserver(obs: AdObserver)
    fun deleteObserver(obs: AdObserver)
    fun requestAd()
    fun requestIgnoreAd()
    fun requestNoAd()
    fun requestShowcase()
}