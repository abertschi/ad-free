## Build

Build instructions for ad-free

### Docker
```sh
# with docker installed
./scripts/build-docker.sh
```

### Android Studio
Tested with Android Studio Quail 3 | 2026.1.3.
- Build locally on Linux with `scripts/build.sh`

- Then set SDK to private setup
  - `chown -R <user>:<group> <project-root>`
  - SDK Manager -> SDK Platform -> Show package details -> API 27
  - SDK Manager -> SDK Tools -> Show package details -> Build Tools 35.0.0
  - SDK Manager -> SDK Tools -> install platform-tools
  - Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> JDK 17
  - local.properties -> sdk.dir should point to scripts/build/sdk-tools-linux
