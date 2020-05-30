#!/bin/bash

curl \
 -X DELETE \
 -F "token=$DEPLOY_GATE_TOKEN" \
 -F "distribution_name=$GITHUB_HEAD_REF" \
 https://deploygate.com/api/users/$DEPLOY_GATE_USER_NAME/platforms/android/apps/jp.kuaddo.tsuidezake/distributions