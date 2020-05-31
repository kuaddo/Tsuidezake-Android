#!/bin/bash

# 一旦fetchしないと対象のコミットを見ることができない
git fetch --prune --unshallow
COMMIT_MESSAGE=`git log --pretty=format:%s -n2 | tail -n1`
echo "::set-env name=DEPLOY_MESSAGE::$COMMIT_MESSAGE"
echo "commit message = $COMMIT_MESSAGE"

if [[ $COMMIT_MESSAGE == *\[Deploy\]* || $COMMIT_MESSAGE == *\[deploy\]* ]]; then
  RESULT=`curl \
    -F "token=$DEPLOY_GATE_TOKEN" \
    -F "file=@app/build/outputs/apk/debug/app-debug.apk" \
    -F "message=$COMMIT_MESSAGE" \
    https://deploygate.com/api/users/$DEPLOY_GATE_USER_NAME/apps`
  ERROR=`echo $RESULT | jq .error`
  if [[ $ERROR == true ]]; then DEPLOY_SUCCESS=false; else DEPLOY_SUCCESS=true; fi
  echo "::set-env name=DEPLOY_SUCCESS::$DEPLOY_SUCCESS"
  if [[ $ERROR == true ]]; then echo $RESULT; fi
else
  echo "::set-env name=DEPLOY_SUCCESS::false"
  echo "Did not upload APK to DeployGate"
fi
