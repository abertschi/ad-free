package ch.abertschi.adump.plugin.interdimcable

import ch.abertschi.adump.plugin.AdPlugin
import ch.abertschi.adump.plugin.PluginContet
import ch.abertschi.adump.view.AppSettings


/**
 * Created by abertschi on 21.04.17.
 */
class InterdimCablePlugin : AdPlugin {

    private val RAW_SUFFIX: String = "?raw=true"
    private val BASE_URL: String = AppSettings.AD_FREE_RESOURCE_ADRESS + "plugins/interdimensional-cable/"
    private val PLUGIN_FILE_PATH: String = BASE_URL + "plugin.yaml" + RAW_SUFFIX

    override fun title(): String = "interdimensional cable"

    override fun onPluginActivated(context: PluginContet) {
//        context.applicationContext.longToast("It's time to get schwifty")
        hasUpdates()
    }

    private fun hasUpdates() {

    }

//    private fun loadModel(): Future<Pair<InterdimCableModel, String>> {
//
////        val f: Future<Pair<InterdimCableModel, String>> = doAsyncResult {
////            var returnVal: Pair<InterdimCableModel, String>? = null
////
////            PLUGIN_FILE_PATH.httpGet().responseString { request, response, result ->
////                val (bytes, error) = result
////                if (error != null) {
////                    throw error
////                } else {
////                    val representer = Representer()
////                    representer.propertyUtils.setSkipMissingProperties(true)
////                    val yaml: Yaml = Yaml(representer)
////                    val model = yaml.loadAs(bytes, InterdimCableModel::class.java)
////                    returnVal = Pair(model, yaml.dump(model))
////                }
////            }
////            return Future()
////        }
//    }

    override fun onPluginDeactivated(context: PluginContet) {}

    override fun play(context: PluginContet) {
    }

    override fun requestStop(contet: PluginContet, onStoped: () -> Unit) {
    }

    override fun forceStop(context: PluginContet) {
    }

}