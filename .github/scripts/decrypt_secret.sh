#!/bin/bash

# Decrypt debug.keystore
gpg --quiet --batch --yes --decrypt --passphrase="$GPG_SECRET" --output ./keystore/debug.keystore ./keystore/debug.keystore.gpg
gpg --quiet --batch --yes --decrypt --passphrase="$GPG_SECRET" --output ./app/google-services.json ./app/google-services.json.gpg
gpg --quiet --batch --yes --decrypt --passphrase="$GPG_SECRET" --output ./authui/secret.properties ./authui/secret.properties.gpg
