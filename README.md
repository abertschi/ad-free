# ad-free
An ad free spotify music experience for Android

<img src=".github/cover.png" width="900">

## Features
- [x] Turn off sound when advertisement is playing
- [ ] Play local music instead when advertisement is playing

## Implementation notes
Advertisment detectors are modularized into implementations of [AdDetectable](./app/src/main/java/ch/abertschi/adump/detector/AdDetectable.kt). An instance of `AdDetectable` can determine if a track being played is a Spotify advertisment or not.

As for now, an `AdDetectable` which [parses notification content](./app/src/main/java/ch/abertschi/adump/detector/SpotifyTitleDetector.kt) for the keyword _Spotify_ is implemented. False positive matches can be manually filtered out.

A more sophisticated `AdDetectable` might use reflection to scan the Spotify notification for properties which are always present when advertisment is played.

## Release notes
### [v0.0.1, 2017-04-17](https://github.com/abertschi/ad-free/releases/tag/v0.0.1)
Initial release
- Turns off sound when advertisement is playing
- Adds notification action to filter out false postive matches
