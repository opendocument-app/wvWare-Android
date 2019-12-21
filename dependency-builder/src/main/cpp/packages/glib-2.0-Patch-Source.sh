#!/bin/sh
set -eu

MESON_BUILD_FILE=$1/meson.build

if test "$ANDROID_NATIVE_API_LEVEL" -lt "21"
then
  echo "Patching $MESON_BUILD_FILE to not use stpcpy. Meson detects it, however it is not avail."
  sed -i "s/glib_conf.set('HAVE_STPCPY', 1)/#glib_conf.set('HAVE_STPCPY', 1)/g" $MESON_BUILD_FILE
fi

