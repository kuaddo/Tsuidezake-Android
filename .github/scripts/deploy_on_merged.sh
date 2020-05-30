#!/bin/sh

# 一旦fetchしないと対象のコミットを見ることができない
git fetch --prune --unshallow

curl \
  -F "token=${{secrets.DEPLOY_GATE_TOKEN}}" \
  -F "file=@app/build/outputs/apk/debug/app-debug.apk" \
  -F "message=${GITHUB_HEAD_REF}" \
  https://deploygate.com/api/users/${{secrets.DEPLOY_GATE_USER_NAME}}/apps;