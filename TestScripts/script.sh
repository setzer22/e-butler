#!/bin/bash
VAR=$(ls ~ | wc)
echo "{ \"var\": \"$VAR\"}"

