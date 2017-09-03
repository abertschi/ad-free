import android.content.Context
import android.service.notification.StatusBarNotification
import ch.abertschi.adfree.ad.AdDetector
import ch.abertschi.adfree.ad.AdEvent
import ch.abertschi.adfree.ad.AdObservable
import ch.abertschi.adfree.ad.AdObserver
import ch.abertschi.adfree.detector.AdDetectable
import ch.abertschi.adfree.detector.AdPayload
import ch.abertschi.adfree.detector.NotificationBundleAndroidTextDetector
import ch.abertschi.adfree.model.RemoteManager
import com.thoughtworks.xstream.XStream
import spock.lang.Specification

/*
 * Ad Free
 * Copyright (c) 2017 by abertschi, www.abertschi.ch
 * See the file "LICENSE" for the full license governing this code.
 */

class NotificationBundleAndroidTextDetectorTest extends Specification implements AdObserver {

    Context contextStub = GroovyStub(Context)
    RemoteManager remoteManager = GroovyStub(RemoteManager)
    List<AdDetectable> detectors = new ArrayList<>()
    LinkedList<AdEvent> happenedEvents = new LinkedList<>()

    def setup() {
        detectors.add(new NotificationBundleAndroidTextDetector())
        happenedEvents = new LinkedList<>()
    }

    def void onAdEvent(AdEvent event, AdObservable o) {
        happenedEvents.add(event)
    }

    def "test notification bundle detector for valid ad detection"() {
        given:
        def detector = new AdDetector(detectors, remoteManager)
        def payload = new AdPayload(GroovyStub(StatusBarNotification))
        print(new XStream().fromXML(""))

        when:
        detector.addObserver(this)
        detector.applyDetectors(payload)

//        then:
//        happenedEvents.size() != 0
    }
}