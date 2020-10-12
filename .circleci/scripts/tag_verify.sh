#!/bin/bash
set -x

VERSION=v$(awk '/version '\''/ {gsub("'\''",""); print $2}' build.gradle)
echo "Pushing tag version is " $CIRCLE_TAG

echo "version on build.gradle" $VERSION

if [ $VERSION == ${CIRCLE_TAG%-*} ]; then
  echo "Pushing tag and Real version match!"
else
  echo "It's not same version."
  exit 1
fi
