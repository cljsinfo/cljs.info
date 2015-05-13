#!/bin/bash
export PATH=/home/oakmac/bin:$PATH
forever list | grep 'b5fc7-5b0bd' &> /dev/null
if [ $? == 0 ]; then
  echo 'forever is running'
else
  echo 'forever not running; starting now...'
  cd /home/oakmac/cljs.info/
  forever start --append --watch --uid 'b5fc7-5b0bd' app.js
fi
