#!/bin/bash

lim=100
list="original/10/FINAL original/5/FINAL"

for i in $list; do

        totline=`wc -l $i | cut -d" " -f1`
        echo "File to process $i with trans $totline"

        awk -v lim=$lim -v totline=$totline -v arq=$i 'BEGIN { FS=":" } ; {  if ( NR >= lim && NR <= (totline - lim) ) dif+=($4 - $3) } ; END { print dif/(totline - (2*lim)) }' $i

done

# arch-tag: 25c30271-2b0b-4518-8758-1e041d55a78b
