#!/bin/bash

if [ -f ./scripts/funcs.sh ]; then
	. ./scripts/funcs.sh 
else
	echo "Some configuration files are missing."
	exit 1
fi

usage $1

echo 
echo "Computing statistics for file $logfile"

certabort=`grep "certification failure" $logfile | wc -l`
certcommit=`grep ":commit:w:" $logfile | wc -l`
multiver=`grep "concurrent update" $logfile | wc -l`
customer=`grep "Customer not found" $logfile | wc -l`
tpcc=`grep "TPC-C Generated" $logfile | wc -l`

echo
echo "$certabort write transactions aborted (certification)."
echo "$multiver write transactions aborted (multiversion)."
echo "`expr $customer + $tpcc` tpc-c aborts."
echo "$certcommit write transactions committed."
echo

# arch-tag: 48123df4-c35c-487c-96f7-bfa66f5d272e
