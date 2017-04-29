/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump

import com.thoughtworks.xstream.XStream

/**
 * Created by abertschi on 29.04.17.
 */
class Serializer {

    private object Holder {
        val INSTANCE = Serializer()
    }

    companion object {
        val instance: Serializer by lazy { Holder.INSTANCE }
    }

    private val xstream: XStream = XStream()

    fun prettyPrint(obj: Any): String {
        return xstream.toXML(obj)
    }
}
