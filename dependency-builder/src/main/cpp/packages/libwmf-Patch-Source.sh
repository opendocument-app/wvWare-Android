#!/bin/sh
set -eu

THIS_FILE=$(readlink -f "$0")
BASEDIR=$(dirname "$THIS_FILE")

patch -p0 < $BASEDIR/libwmf-Patch-Source-no-libjpeg.patch

