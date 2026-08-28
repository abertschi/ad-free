#!/bin/bash
set -euo pipefail

#
# XXX: Captures a working build of the project
#
readonly SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
readonly PROJ_ROOT=$SCRIPT_DIR/../
readonly BUILD_DIR=${BUILD_DIR:-$SCRIPT_DIR/build}

readonly JDK_URL="https://api.adoptium.net/v3/binary/latest/17/ga/linux/x64/jdk/hotspot/normal/eclipse?project=jdk"
readonly CMD_LINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-15859902_latest.zip"
readonly SDK_MANAGER=sdk-tools-linux/cmdline-tools/latest/bin/sdkmanager
readonly COMPILE_SDK=27
readonly BUILD_TOOLS=35.0.0

set -x
mkdir -p "$BUILD_DIR"
cd "$BUILD_DIR"

export HOME=$BUILD_DIR/home
mkdir -p "$HOME"

readonly JDK_DIR=$BUILD_DIR/jdk-17
if [[ ! -d $JDK_DIR ]]; then
    wget "${JDK_URL}" -O jdk.tar.gz
    mkdir -p "$JDK_DIR"
    tar -xzf jdk.tar.gz -C "$JDK_DIR" --strip-components=1
    rm -f jdk.tar.gz
fi
export JAVA_HOME=$JDK_DIR
export PATH=$JAVA_HOME/bin:$PATH
export JAVA_TOOL_OPTIONS=-Djava.net.preferIPv4Stack=true

readonly SDK_DIR=$BUILD_DIR/sdk-tools-linux
if [[ ! -d $SDK_DIR ]]; then
    wget ${CMD_LINE_TOOLS_URL} -O tools.zip
    unzip -qq tools.zip -d tools-extract
    mkdir -p sdk-tools-linux/cmdline-tools
    mv tools-extract/cmdline-tools sdk-tools-linux/cmdline-tools/latest
    rm -rf tools.zip tools-extract
fi

yes | ${SDK_MANAGER} --licenses > /dev/null || true
${SDK_MANAGER} --install "platform-tools" "platforms;android-${COMPILE_SDK}" "build-tools;${BUILD_TOOLS}" > /dev/null
yes | ${SDK_MANAGER} --licenses > /dev/null || true

export GRADLE_USER_HOME=$BUILD_DIR/gradle-home

export ANDROID_HOME=$PWD/sdk-tools-linux
unset ANDROID_SDK_ROOT
export PATH=$PATH:$PWD/sdk-tools-linux/platform-tools/

cd "$PROJ_ROOT"
chmod +x ./gradlew
echo "sdk.dir=${ANDROID_HOME}" > local.properties

./gradlew test
./gradlew assembleDebug

ls -la "$PROJ_ROOT/app/build/outputs/apk/debug/"
cp -rf "$PROJ_ROOT/app/build/outputs/apk/debug/" $BUILD_DIR/
