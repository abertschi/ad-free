# Changelog / Ad-Free

### v2.0/34, 2020-02-10
- add option to choose which detectors are active (#50)
- add option to delay unmute for a few seconds (#39)
- add option for always-on notification to prevent ad-free from being killed by
  OS. needed for devices such as MIUI. this includes a boot_completed
  receiver, alarmservice timer and runs the
  notification listener service in foreground (#51)
- overhaul of music plugin, use system own ACTION_OPEN_DOCUMENT_TREE
  dialog to choose music directory (#44)
- add option to loop local music plugin until end of ad (#48)  
- change how audio is muted. use setStreamVolume instead of
  adjustStreamVolume (#41)
- increase minSdkVersion to 23/ Android 6.0 (#19)
- UI optimized for tablets

### v1.2.1/33,  2019-11-9
- update fdroid metadata: screenshots, changelog
- no functional changes

### v1.2, 2019-11-03
- bug fix: fix casting issue on non-miui devices

### v1.1, 2019-10-30
- Add support for miui based devices
- introduce global error handler and option for email bug report on crash
- introduce sc ad detection
- introduce option to dump spotify notification

### v1.0, 2019-06-27
- Update fdroid store information (thanks @bennettscience)
- Introduce more supported audio formats for local music: mp3, wav,
  m4a
- internal: update build tools

### v0.0.4.6, 2018-02-10
- Fix notification issue introduced with version 0.0.4.4. Ad blocking notification was not shown.

### v0.0.4.5, 2018-02-05
- fdroid release only, fixes dependency issues causing 0.0.4.4 not to build on fdroidserver.

### v0.0.4.4, 2018-01-27
- Internal changes, compile project with sdkVersion 27 (android Oreo)

### v0.0.4.3, 2018-01-02
- Change selection of default folder in localmusic plugin from
  `/storage` to `/`. `/storage` was not available on some devices like Nexus 6/7.1.1.

### v0.0.4.2, 2017-09-10
- Bugfixes
- Removed Google dependencies and prepared for F-Droid  store release

### v0.0.4.1, 2017-09-01
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
