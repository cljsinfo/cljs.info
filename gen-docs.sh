#!/bin/sh

git submodule init
git submodule update

cd scripts/cljs-api-parser
lein run
