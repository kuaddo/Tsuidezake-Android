#!/bin/bash

# 一旦fetchしないと対象のコミットを見ることができない
git fetch --prune --unshallow
COMMIT_MESSAGE=`git log --pretty=format:%s -n1 $COMMIT_HASH`
echo "commit message = $COMMIT_MESSAGE"

if [[ $COMMIT_MESSAGE == *\[Deploy\]* || $COMMIT_MESSAGE == *\[deploy\]* ]]; then
  DISTRIBUTION_URL=`curl \
    -F "token=$DEPLOY_GATE_TOKEN" \
    -F "file=@app/build/outputs/apk/debug/app-debug.apk" \
    -F "message=$COMMIT_MESSAGE" \
    -F "distribution_name=$GITHUB_HEAD_REF" \
    https://deploygate.com/api/users/$DEPLOY_GATE_USER_NAME/apps \
  | jq -r '.results.distribution.url'`
  echo "distribution url = $DISTRIBUTION_URL"
  echo "::set-env name=DISTRIBUTION_URL::$DISTRIBUTION_URL"
  echo "::set-env name=DEPLOY_MESSAGE::$COMMIT_MESSAGE"
else
  echo "Did not upload APK to DeployGate"
fi