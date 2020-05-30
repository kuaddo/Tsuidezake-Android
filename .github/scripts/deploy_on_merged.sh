#!/bin/bash

curl \
  -F "token=$DEPLOY_GATE_TOKEN" \
  -F "file=@app/build/outputs/apk/debug/app-debug.apk" \
  -F "message=$PR_TITLE" \
  https://deploygate.com/api/users/$DEPLOY_GATE_USER_NAME/apps
echo "::set-env name=DEPLOY_MESSAGE::$PR_TITLE"
