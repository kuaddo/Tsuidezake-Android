#!/bin/bash

# 一旦fetchしないと対象のコミットを見ることができない
git fetch --prune --unshallow
COMMIT_MESSAGE=`git log --pretty=format:%s -n1 $COMMIT_HASH`
echo "commit message = $COMMIT_MESSAGE"

if [[ $COMMIT_MESSAGE == *\[Deploy\]* || $COMMIT_MESSAGE == *\[deploy\]* ]]; then
  curl \
    -F "token=$DEPLOY_GATE_TOKEN" \
    -F "file=@app/build/outputs/apk/debug/app-debug.apk" \
    -F "message=$COMMIT_MESSAGE" \
    https://deploygate.com/api/users/$DEPLOY_GATE_USER_NAME/apps
  echo "::set-env name=DEPLOY_MESSAGE::$COMMIT_MESSAGE"
else
  echo "Did not upload APK to DeployGate"
fi