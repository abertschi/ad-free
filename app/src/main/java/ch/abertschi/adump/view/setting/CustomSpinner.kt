package ch.abertschi.adump.view.setting

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner

/**
 * Created by abertschi on 23.04.17.
 */
class CustomSpinner @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Spinner(context, attrs, defStyleAttr) {

    var selectedIndex: Int = 0

    open fun onDetechedFromWindow() {
        super.onDetachedFromWindow()
    }
}