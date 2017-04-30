/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.util

import com.thoughtworks.xstream.XStream

/**
 * Created by abertschi on 29.04.17.
 */
class Serializer {

    private object Holder {
        val INSTANCE = ch.abertschi.adump.util.Serializer()
    }

    companion object {
        val instance: ch.abertschi.adump.util.Serializer by lazy { ch.abertschi.adump.util.Serializer.Holder.INSTANCE }
    }

    private val xstream: com.thoughtworks.xstream.XStream = com.thoughtworks.xstream.XStream()

    fun prettyPrint(obj: Any): String {
        return xstream.toXML(obj)
    }
}
