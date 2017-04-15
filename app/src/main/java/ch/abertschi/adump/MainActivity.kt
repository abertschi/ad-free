//package ch.abertschi.adump
//
//import android.content.Intent
//import android.graphics.Color
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.TextView
//import android.text.Html
//import android.graphics.Typeface
//import android.os.Build
//import android.provider.Settings
//import android.provider.Settings.Secure
//import android.view.View
//import org.jetbrains.anko.toast
//
//
//class MainActivity : AppCompatActivity() {
//
//    lateinit var mTypeFace: Typeface
//    lateinit var mActivateButton: TextView
//    lateinit var mEnjoySloganText: TextView
//    var enabled: Boolean = true
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.navigationBarColor = Color.parseColor("#252A2E")
//        }
//
//        mActivateButton = findViewById(R.id.permission) as TextView
//        mTypeFace = Typeface.createFromAsset(assets, "fonts/Raleway-ExtraLight.ttf")
//        mActivateButton!!.typeface = mTypeFace
//        mEnjoySloganText = findViewById(R.id.enjoy) as TextView
//
//        mActivateButton!!.setOnClickListener {
//            if (enabled) {
//                enabled = false
//                mActivateButton!!.text = "enable"
//            } else {
//                enabled = true
//                mActivateButton!!.text = "disable"
//                toast("listening for ads")
//            }
//        }
//
//        showPermissionRequiredIfNecessary()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        println("onActivityResult")
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    fun hasNotificationPermission(): Boolean {
//        val permission = Settings.Secure.getString(this.contentResolver, "enabled_notification_listeners")
//        if (permission == null || !permission.contains(packageName)) {
//            return false
//        }
//        return true
//    }
//
//    fun showSloganText() {
//        val text = "enjoy your <font color=#FFFFFF>ad free</font> music experience"
//        setSloganText(text)
//    }
//
//    fun showPermissionText() {
//        val text = "touch here to grant permission"
//        setSloganText(text)
//    }
//
//    fun setSloganText(text: String) {
//        mEnjoySloganText!!.typeface = mTypeFace
//        mEnjoySloganText!!.text = Html.fromHtml(text)
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        showPermissionRequiredIfNecessary()
//    }
//
//    fun showPermissionRequiredIfNecessary() {
//        if (!hasNotificationPermission()) {
//            showPermissionText()
//            mActivateButton!!.text = ""
//            mEnjoySloganText!!.setOnClickListener {
//                println("pressed")
//                startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
//            }
//        } else {
//            showSloganText()
//            mEnjoySloganText!!.setOnClickListener(null)
//            mActivateButton!!.text = "disable"
//        }
//    }
//}