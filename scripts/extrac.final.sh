#!/bin/bash

list="1.client/1.replicas/5/client-01.log 1.client/1.replicas/10/client-01.log 1.client/2.replicas/5/client-01.log 1.client/2.replicas/10/client-01.log 2.client/2.replicas/5/client-01.log 2.client/2.replicas/5/client-02.log original/1.replicas/5/client-01.log original/1.replicas/10/client-01.log"

lim=100

for i in $list; do

	awk ' 
	BEGIN { for (i = 0 ; i < 10 ; i++) { str[i]="INFO" } } 
	{	if ($3 == "WARN") { 
			if ($2 == "[TPC-C-0]")	{ str[0] = "WARN" }
			else if ($2 == "[TPC-C-1]")  { str[1] = "WARN" }
			else if ($2 == "[TPC-C-2]")  { str[2] = "WARN" }
			else if ($2 == "[TPC-C-3]")  { str[3] = "WARN" }
			else if ($2 == "[TPC-C-4]")  { str[4] = "WARN" }
			else if ($2 == "[TPC-C-5]")  { str[5] = "WARN" }
			else if ($2 == "[TPC-C-6]")  { str[6] = "WARN" }
			else if ($2 == "[TPC-C-7]")  { str[7] = "WARN" }
			else if ($2 == "[TPC-C-8]")  { str[8] = "WARN" }
			else if ($2 == "[TPC-C-9]")  { str[9] = "WARN" }
		}
		else if ($4 == "escada.tpc.common.database.CommonDatabase") {
			if ($2 == "[TPC-C-0]")	{ if (str[0] != "WARN") print $0; else str[0] = "INFO" }
			else if ($2 == "[TPC-C-1]") { if (str[1] != "WARN") print $0; else str[1] = "INFO" }
			else if ($2 == "[TPC-C-2]") { if (str[2] != "WARN") print $0; else str[2] = "INFO" }
			else if ($2 == "[TPC-C-3]") { if (str[3] != "WARN") print $0; else str[3] = "INFO" }
			else if ($2 == "[TPC-C-4]") { if (str[4] != "WARN") print $0; else str[4] = "INFO" }
			else if ($2 == "[TPC-C-5]") { if (str[5] != "WARN") print $0; else str[5] = "INFO" }
			else if ($2 == "[TPC-C-6]") { if (str[6] != "WARN") print $0; else str[6] = "INFO" }
			else if ($2 == "[TPC-C-7]") { if (str[7] != "WARN") print $0; else str[7] = "INFO" }
			else if ($2 == "[TPC-C-8]") { if (str[8] != "WARN") print $0; else str[8] = "INFO" }
			else if ($2 == "[TPC-C-9]") { if (str[9] != "WARN") print $0; else str[9] = "INFO" }
		}
	}
	' $i > $i.temp

	cut -d"-" -f4 $i.temp | sed "s/ //" > $i.final

        totline=`wc -l $i.final | cut -d" " -f1`

        echo "File to process $i with trans $totline"

        awk -v lim=$lim -v totline=$totline -v arq=$i.final 'BEGIN { FS=":" } ; {  if ( NR >= lim && NR <= (totline - lim) ) dif+=($4 - $3) } ; END { print dif/(totline - (2*lim)) }' $i.final

done

# arch-tag: b953083e-3b41-4874-99e6-ea011fbb3655
