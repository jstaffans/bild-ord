#!/bin/sh

echo "Busting cache ..."

GIT_REF=$(git rev-parse --short HEAD)
grep -lR "_version" src resources | xargs sed -i '' s/_version=\.\*\"/_version=${GIT_REF}\"/g
