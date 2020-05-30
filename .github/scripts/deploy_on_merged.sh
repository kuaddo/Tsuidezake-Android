#!/bin/bash

curl \
  -F "token=$DEPLOY_GATE_TOKEN" \
  -F "file=@app/build/outputs/apk/debug/app-debug.apk" \
  -F "message=$GITHUB_HEAD_REF" \
  https://deploygate.com/api/users/$DEPLOY_GATE_USER_NAME/apps;