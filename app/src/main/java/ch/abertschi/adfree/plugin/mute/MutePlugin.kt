/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.mute

import ch.abertschi.adfree.plugin.AdPlugin

/**
 * Created by abertschi on 21.04.17.
 */
class MutePlugin : AdPlugin {
    override fun stop(onStoped: () -> Unit) {
    }

    override fun onPluginLoaded() {
    }

    override fun title() = "mute audio"

    override fun play() {
    }

    override fun playTrial() {
    }

    override fun requestStop(onStoped: () -> Unit) {
        onStoped()
    }

    override fun forceStop(onStoped: () -> Unit) {
    }

    override fun onPluginActivated() {
    }

    override fun onPluginDeactivated() {
    }
}