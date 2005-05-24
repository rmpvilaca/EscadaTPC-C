#!/bin/bash

if [ -f ./scripts/funcs.sh ]; then
        . ./scripts/funcs.sh
else
        echo "Some configuration files are missing."
        exit 1
fi

usage $1

lim=100
list=":beginning:w: :beginning:r: :processing:w: :processing:r: :committing:w: :committing:r: :aborting:w: :aborting:r: :commit:r: :commit:w:"

for filter in $list; do

	echo 
	echo "Computing $filter for $logfile"
	grep "$filter" $logfile | cut -d" " -f7-8 > templog
        totline=`wc -l templog | cut -d" " -f1`

	echo "Transactions $totline and limit $lim"

        awk -v lim=$lim -v totline=$totline -v arq=$logfile.$cont 'BEGIN { FS=":" } ; {  if ( NR >= lim && NR <= (totline - lim) ) dif += $4 } ; END { print dif/(totline - (2*lim)) }' templog

	rm -rf templog
done

echo

# arch-tag: ea2bbe7e-57ac-4b04-b19f-20907c7fb35b


