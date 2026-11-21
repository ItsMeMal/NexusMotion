#!/usr/bin/env sh
set -eu
GRADLE_VERSION="8.14.8"
CACHE_DIR="${GRADLE_USER_HOME:-$HOME/.gradle}/nexusmotion"
GRADLE_HOME="$CACHE_DIR/gradle-$GRADLE_VERSION"
DIST_URL="https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
ZIP_FILE="$CACHE_DIR/gradle-$GRADLE_VERSION-bin.zip"

if [ ! -x "$GRADLE_HOME/bin/gradle" ]; then
  mkdir -p "$CACHE_DIR"
  echo "Downloading Gradle $GRADLE_VERSION from the official Gradle distribution host..."
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$DIST_URL.sha256" -o "$ZIP_FILE.sha256"
    curl -fL "$DIST_URL" -o "$ZIP_FILE"
  else
    echo "curl is required to bootstrap Gradle." >&2
    exit 1
  fi
  EXPECTED="$(awk '{print $1}' "$ZIP_FILE.sha256" | tr '[:upper:]' '[:lower:]')"
  ACTUAL="$(sha256sum "$ZIP_FILE" | awk '{print $1}' | tr '[:upper:]' '[:lower:]')"
  if [ "$EXPECTED" != "$ACTUAL" ]; then
    rm -f "$ZIP_FILE" "$ZIP_FILE.sha256"
    echo "Gradle distribution checksum mismatch." >&2
    exit 1
  fi
  unzip -q "$ZIP_FILE" -d "$CACHE_DIR"
  rm -f "$ZIP_FILE" "$ZIP_FILE.sha256"
fi

exec "$GRADLE_HOME/bin/gradle" "$@"
