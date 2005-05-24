#!/bin/bash

usage () {
	if [ "$1" = "" ]; then
        	echo "You must inform the log file."
	        echo "Usage: statistics.sh logfile"
        	exit 1
	else
        	logfile=$1
	        if [ ! -f $logfile ]; then
        	        echo "$logfile is not a valid log file."
                	exit 1
	        fi
	fi
}

# arch-tag: 4e563a65-68c4-4537-a1f4-28c4f3133346

