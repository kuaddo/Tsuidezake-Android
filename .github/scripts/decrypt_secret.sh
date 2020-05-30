#!/bin/bash

# Decrypt debug.keystore
gpg --quiet --batch --yes --decrypt --passphrase="$GPG_SECRET" --output ./keystore/debug.keystore ./keystore/debug.keystore.gpg
