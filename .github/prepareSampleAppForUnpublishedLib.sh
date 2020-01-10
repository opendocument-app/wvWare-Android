#!/bin/sh
set -eu

THIS_FILE=$(readlink -f "$0")
BASEDIR=$(readlink -f $(dirname "$THIS_FILE")/..)

sed -E "/repositories \{/a mavenLocal()" -i $BASEDIR/sample-app/build.gradle

# Extract project version from build.gradle
VERSION=$($BASEDIR/getVersion.sh)
version_format='([0-9\.]+)'
# Update version in sample app's dependency list
expression="s/implementation 'com.viliussutkus89:wvware-android:$version_format'/implementation 'com.viliussutkus89:wvware-android:$VERSION'/"
sed -E "--expression=$expression" -i $BASEDIR/sample-app/app/build.gradle

