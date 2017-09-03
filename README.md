[![Kotlin App](https://img.shields.io/badge/Android-Kotlin-green.svg?style=flat)]()  [![codebeat badge](https://codebeat.co/badges/1fc357d9-4c2e-46f6-b847-d295e4de78eb)](https://codebeat.co/projects/github-com-abertschi-ad-free-master)
# Ad-Free

Ad Free is a research project attempting to show flaws in the way how Spotify for Android presents audio advertisement. It is a proof-of-concept of a modularized Ad Blocker written in Kotlin with a modern and simplistic user interface.

<img src=".github/cover2.png" width="900">

## Features
- No ROOT required
- Turn off sound when advertisement is playing
- Play arbitrary audio instead of Spotify advertisement
- Plugin based design

## Download
This is OpenSource. [Download the latest binary from the release tab](https://github.com/abertschi/ad-free/releases/latest) or clone and build it yourself :fire:

## Implementation notes
### Ad detection
Advertisement detectors are modularized into implementations of [AdDetectable](./app/src/main/java/ch/abertschi/adfree/detector/AdDetectable.kt). An instance of `AdDetectable` can determine if a track being played is a Spotify advertisement or not.

Ad Free registers an [NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService.html) and is therefore able to parse all incoming notifications on Android. Notifications shown by Spotify are parsed by implementations of `AdDetectable`:

- `SpotifyTitleDetector`:  
Detector which parses Spotify notifications for certain keywords. In order to avoid false positives, an notification action is provided to unblock wrongly detected advertisements.

- `NotificationActionDetector`:  
Detector which inspects Spotify notifications for properties set in the track navigation bar.

- `NotificationBundleAndroidTextDetector`:  
Detector which checks for properties set in the notification bundle.

### Ad blocking
[AudioManager](https://developer.android.com/reference/android/media/AudioManager.html), Android's Audio System provides several streams on which audio can be played. Spotify, alike many music players, plays audio on the stream [STREAM_MUSIC](https://developer.android.com/reference/android/media/AudioManager.html#STREAM_MUSIC). In case of ad detection, Ad Free mutes _STREAM MUSIC_ and calls a configured [AdPlugin](./app/src/main/java/ch/abertschi/adfree/plugin/AdPlugin.kt). `AdPlugins` aim to replace Spotify's advertisement. They play music on an alternative stream and are therefore not affected by the mute of _STREAM MUSIC_.

#### Plugins

##### Mute Audio
As the title of this plugin suggests, it only mutes adertisments without playing sound.

##### Interdimensional Cable
[Interdimensional Cable](./app/src/main/java/ch/abertschi/adfree/plugin/interdimcable/InterdimCablePlugin.kt) replaces Spotify advertisements with interdimensional cable advertisements featured in [Adult Swim's Rick and Morty TV series](https://www.youtube.com/watch?v=sBvV1miNoA8&index=12&list=PLNu47mcqeyiATtjW5pIRWlpXBu4pUezdP).

##### Play local music
Play music tracks stored on your phone while advertisement is playing

## Compatibility
Due to the lack of notifications on Android TV, Ad Free is currently not compatible with Android TV.

Tested with the following Spotify versions
- 2017-04-19: 8.3.0.681 armV7
- 2017-09-01: 8.4.17.640 armV7

## Release notes
### v0.0.4.0, 2017-09-01
- Introduce a new Plugin: `local music` to play music stored on your phone while
ads are being played.
- Improve ad detection. Implement finite state machine to keep track of an ad is currently played or not.
- Introduce landing webpage and prepare for play store release.

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

- This app is written in Kotlin. [Kotlin](https://github.com/JetBrains/kotlin) by Jetbrains is lisenced by the Apache Lisence 2.0

- [AppUpdater](https://github.com/javiersantos/AppUpdater) by javiersantos is licensed by the Apache License 2.0

- [Videocache](https://github.com/danikula/AndroidVideoCache/blob/master/LICENSE) is made by danikula and is licensed by the Apache License 2.0

- [RxAndroid](https://github.com/ReactiveX/RxAndroid) by the RxAndroid authors is licensed by the Apache License 2.0
- [Fuel](https://github.com/kittinunf/Fuel) is made by Kittinun Vantasin and is licensed by MIT

- [snakeyaml](https://bitbucket.org/asomov/snakeyaml) is licensed by the Apache License 2.0

- [XStream](http://x-stream.github.io/license.html) by Joe Walnes and the XStream Committers is lisenced by BSD license.

## Licence
This project is lisenced by the Apache Lisence 2.0

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IT IS A PROOF OF CONCEPT AND INTENDED TO BE A RESEARCH PROJECT. IN NO EVENT
SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
DAMAGE.
