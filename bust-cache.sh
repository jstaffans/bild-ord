#!/bin/sh

echo "Busting cache ..."

git rev-parse --short HEAD > resources/cache_version
