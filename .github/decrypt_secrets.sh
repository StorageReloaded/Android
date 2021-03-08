#!/bin/sh
echo "$SECRETS_GPG_PASS" | gpg --quiet --batch --yes --decrypt --passphrase-fd 0 .github/secrets_overlay.tar.xz.gpg | tar xJ -C .
