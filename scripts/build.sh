#!/bin/bash
set -euo pipefail

# requires jdk 11 or later

readonly SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
readonly PROJ_ROOT=$SCRIPT_DIR/../
readonly BUILD_DIR=$SCRIPT_DIR/build

readonly CMD_LINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip"
readonly SDK_MANAGER=sdk-tools-linux/cmdline-tools/latest/bin/sdkmanager

set -x
mkdir -p $BUILD_DIR
cd $BUILD_DIR


readonly SDK_DIR=$BUILD_DIR/sdk-tools-linux
if [[ ! -d $SDK_DIR ]]; then
    wget --quiet ${CMD_LINE_TOOLS_URL} -O tools.zip
    unzip -qq tools.zip -d sdk-tools-linux
    mkdir sdk-tools-linux/cmdline-tools/latest
    mv sdk-tools-linux/cmdline-tools/* sdk-tools-linux/cmdline-tools/latest/  || true
    yes | ${SDK_MANAGER} --licenses > /dev/null || true
    ${SDK_MANAGER} --install ndk-bundle > /dev/null
    yes | ${SDK_MANAGER} --licenses  > /dev/null || true
fi

export ANDROID_HOME=$PWD/sdk-tools-linux
unset ANDROID_SDK_ROOT
export PATH=$PATH:$PWD/sdk-tools-linux/platform-tools/

cd $PROJ_ROOT
chmod +x ./gradlew

# build
./gradlew test
./gradlew assembleDebug

