package ch.abertschi.adump.plugin.interdimcable

/**
 * Created by abertschi on 22.04.17.
 */
data class InterdimCableModel(val channels: List<Channel>? = null) {
    constructor() : this(listOf<Channel>())
}

data class Channel(var path: String? = null, var name: String? = null, var version: Int? = null) {
    constructor() : this(null, null, null)
    constructor(path: String?) : this(path, null, null)
}

