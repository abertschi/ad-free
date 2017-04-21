package ch.abertschi.adump.di

import android.content.Context
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.presenter.ControlPresenter
import ch.abertschi.adump.view.home.ControlView

/**
 * Created by abertschi on 15.04.17.
 */

class ControlModul(val context: Context, val controlView: ControlView) {

    fun provideControlPresenter(): ControlPresenter {
        return ControlPresenter(controlView, PreferencesFactory.providePrefernecesFactory(context))
    }

}
