/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

package ch.abertschi.adfree.detector

import org.jetbrains.anko.AnkoLogger

class DummySpotifyDetector:
        AbstractStatusBarDetector(), AnkoLogger {

    override fun flagAsAdvertisement(payload: AdPayload) = true

    override fun getMeta(): AdDetectorMeta
            = AdDetectorMeta("Dummy spotify", "flag all spotify notifications as ads. " +
            "use this to test if notification listener works. for debugging only.",
            enabledByDef = false, debugOnly = true)
}