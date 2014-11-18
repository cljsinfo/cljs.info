#!/bin/bash
scp -P 22 -r 00-publish/css USER@EXAMPLE.COM:/path/to/public
scp -P 22 -r 00-publish/js USER@EXAMPLE.COM:/path/to/public
scp -P 22 -r 00-publish/fonts USER@EXAMPLE.COM:/path/to/public
scp -P 22 -r 00-publish/img USER@EXAMPLE.COM:/path/to/public
scp -P 22 00-publish/favicon.png USER@EXAMPLE.COM:/path/to/public/favicon.png
scp -P 22 -r 00-publish/cheatsheet USER@EXAMPLE.COM:/path/to/public