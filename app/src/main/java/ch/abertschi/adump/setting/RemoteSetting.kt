/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adump.setting

import ch.abertschi.adump.Serializer

/**
 * Created by abertschi on 26.04.17.
 */
class RemoteSetting {

    var useGithubReleasesForUpdateReminder: Boolean = false
    var showSeakbarOnUpdate: Boolean = true
    var showNotificationOnUpdate: Boolean = false

    var showMessageOnStart: Boolean = false
    var messageOnStartTitle: String = ""
    var messageOnStartContent: String = ""

    var showMessageOnUse: Boolean = false
    var messageOnUseTitle: String = ""
    var messageOnUseContent: String = ""

    var enabled: Boolean = true

    override fun toString(): String {
        return Serializer.instance.prettyPrint(this)
    }
}