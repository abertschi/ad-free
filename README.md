[![Kotlin App](https://img.shields.io/badge/Android-Kotlin-green.svg?style=flat)]()
# ad-free

Ad Free is a research project attempting to show flaws in the way how Spotify for Android presents audio advertisement. It is a proof-of-concept of a modularized Ad Blocker written in Kotlin with a modern and simplistic user interface.

<img src=".github/cover2.png" width="900">

## Features
- Turn off sound when advertisement is playing
- Play arbitrary audio instead of Spotify advertisement
- No ROOT required
- Plugin based design

## Download
- Coming soon :fire:

## Implementation notes
### Ad detection
Advertisement detectors are modularized into implementations of [AdDetectable](./app/src/main/java/ch/abertschi/adump/detector/AdDetectable.kt). An instance of `AdDetectable` can determine if a track being played is a Spotify advertisement or not.

Ad Free registers an [NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService.html) and is therefore able to parse all incoming notifications on Android. Notifications shown by Spotify are parsed by implementations of `AdDetectable`:

- `SpotifyTitleDetector`:  
Detector which parses Spotify notifications for certain keywords. In order to avoid false positives, an notification action is provided to unblock wrongly detected advertisements.

- `NotificationActionDetector`:  
Detector which inspects Spotify notifications for properties set in the track navigation bar.

- `NotificationBundleAndroidTextDetector`:  
Detector which checks for properties set in the notification bundle.


### Ad blocking
[AudioManager](https://developer.android.com/reference/android/media/AudioManager.html), Android's Audio System provides several streams on which audio can be played. Spotify, alike many music players, plays audio on the stream [STREAM_MUSIC](https://developer.android.com/reference/android/media/AudioManager.html#STREAM_MUSIC). In case of ad detection, Ad Free mutes _STREAM MUSIC_ and calls a configured [AdPlugin](./app/src/main/java/ch/abertschi/adump/plugin/AdPlugin.kt). `AdPlugins` aim to replace Spotify's advertisement. They play music on an alternative stream and are therefore not affected by the mute of _STREAM MUSIC_.

#### Plugins

Currently, an [instance](./app/src/main/java/ch/abertschi/adump/plugin/interdimcable/InterdimCablePlugin.kt) of `AdPlugin` is implemented which replaces Spotify advertisements with interdimensional cable advertisements featured in [Adult Swim's Rick and Morty TV series](https://www.youtube.com/watch?v=sBvV1miNoA8&index=12&list=PLNu47mcqeyiATtjW5pIRWlpXBu4pUezdP).

## Compatibility
Due to the lack of notifications on Android TV, Ad Free is currently not compatible with Android TV.

Tested with the following Spotify versions
- 2017-04-19: 8.3.0.681 armV7

## Release notes

### v0.0.3.4, 2017-04-30
- Various bug fixes and internal refactoring
- Improve ad detection. Ad blocker now blocks ads shown in 'Your Daily mix' with new ad detector NotificationBundleAndroidTextDetector.

### v0.0.3.0, 2017-04-23
This major release extends the user interface with plugins. Plugins run while ads are being played and
add some level of entertainment to your music experience.

- Replace Spotify advertisements with interdimensional cable advertisement featured in Adult Swim's Rick and Morty TV series.
- SpotifyTitleDetector is deprecated. NotificationActionDetector is capable to detect all ads.
- Optimized for Tablets

### v0.0.2.2, 2017-04-17
Minor release
- Fixing issues with Auto Updater

### v0.0.2, 2017-04-17
- More sophisticated Ad detector implemented.
- Auto update feature available with [AppUpdater](https://github.com/javiersantos/AppUpdater)
- Bug fixing

### v0.0.1, 2017-04-17
Initial release
- Turns off sound when advertisement is playing
- Adds notification action to filter out false positive matches

## Credits
- The bird and website icons used in this app are made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> and are licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a>.

- [AppUpdater](https://github.com/javiersantos/AppUpdater) by javiersantos is licensed by the Apache License 2.0

- [Videocache](https://github.com/danikula/AndroidVideoCache/blob/master/LICENSE) is made by danikula and is licensed by the Apache License 2.0

- [RxAndroid](https://github.com/ReactiveX/RxAndroid) by the RxAndroid authors is licensed by the Apache License 2.0
- [Fuel](https://github.com/kittinunf/Fuel) is made by Kittinun Vantasin and is licensed by MIT

- [snakeyaml](https://bitbucket.org/asomov/snakeyaml) is licensed by the Apache License 2.0
