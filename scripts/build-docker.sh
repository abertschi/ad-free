#!/bin/bash
set -euo pipefail

readonly SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
readonly PROJ_ROOT=$SCRIPT_DIR/../
readonly IMAGE=adfree-build

set -x
docker build -t "$IMAGE" "$PROJ_ROOT"
# --user "$(id -u):$(id -g)"
docker run --rm  -e BUILD_DIR=/repo/scripts/build-docker \
  -v "$PROJ_ROOT":/repo "$IMAGE"
