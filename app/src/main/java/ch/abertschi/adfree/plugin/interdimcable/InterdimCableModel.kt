/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin.interdimcable

import ch.abertschi.adfree.util.Serializer

/**
 * Created by abertschi on 22.04.17.
 */
// Dont use data class attribute because yaml needs default constructor
class InterdimCableModel {
    val channels: List<Channel>? = null
    override fun toString(): String {
        return Serializer.instance.prettyPrint(this)
    }
}

class Channel {
    var path: String? = null
    var name: String? = null
    var version: Int? = null

    override fun toString(): String {
        return Serializer.instance.prettyPrint(this)
    }
}

