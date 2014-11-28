#!/bin/sh

git submodule init
git submodule update

cd scripts/get-symbol-metadata
lein run
