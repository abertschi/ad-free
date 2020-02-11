/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.plugin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Created by abertschi on 30.08.17.
 */
interface PluginActivityAction {

    fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?);

    fun addOnActivityResult(callable: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit)

    fun activity(): Fragment
}