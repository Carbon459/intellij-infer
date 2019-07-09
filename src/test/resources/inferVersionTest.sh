#!/bin/bash

if [ "$1" ==  "--version-json" ] 
	then
	echo "{"
	echo "\"major\": 0,"
	echo "\"minor\": 16,"
	echo "\"patch\": 0,"
	echo "\"commit\": \"4a91616\","
	echo "\"branch\": \"HEAD\","
	echo "\"tag\": \"v0.16.0\""
	echo "}"
fi
