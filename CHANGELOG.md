# Changelog

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
