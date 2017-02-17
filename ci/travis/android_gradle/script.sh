#!/bin/sh

set -e

# http://stackoverflow.com/a/36538247
./gradlew --info externalNativeBuildDebug & PID=$!

echo "gradlew is running..."
printf "["
# While process is running...
while kill -0 $PID 2> /dev/null; do 
    printf  "="
    sleep 1
done
printf "] done!"
