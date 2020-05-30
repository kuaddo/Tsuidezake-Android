#!/bin/bash

DISTRIBUTION_URL=`curl \
  -F "token=$DEPLOY_GATE_TOKEN" \
  -F "file=@app/build/outputs/apk/debug/app-debug.apk" \
  -F "message=$PR_TITLE" \
  -F "distribution_name=$PR_TITLE" \
  https://deploygate.com/api/users/$DEPLOY_GATE_USER_NAME/apps \
  | jq -r '.results.distribution.url'`
echo "distribution url = $DISTRIBUTION_URL"
echo "::set-env name=DISTRIBUTION_URL::$DISTRIBUTION_URL"
echo "::set-env name=DEPLOY_MESSAGE::$PR_TITLE"