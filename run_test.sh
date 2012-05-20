#!/bin/bash

export INSTANCESLOG='examples/network/result/instances.txt'
export INSTANCEPATH='examples/network/test'
export RESULTPATH='examples/network/result'
export INSTANCES="1-2-2-100-a 1-2-3-100-a 1-2-4-100-a 1-3-3-100-a"
export ANSWERS="1"

#empty file
> $INSTANCESLOG

for ins in $INSTANCES ; do
    echo $ins >> $INSTANCESLOG
    for ans in $ANSWERS ; do
	echo Running instance $ins for $ans answers

	sleep 20
	echo Local solver
	bash $INSTANCEPATH/instance-$ins-local.sh $ans > $RESULTPATH/instance-$ins-local-$ans.log 2>&1

	sleep 20
	echo Semin-distributed algorithm
	bash $INSTANCEPATH/instance-$ins-central.sh $ans > $RESULTPATH/instance-$ins-central-$ans.log 2>&1

	sleep 20
	echo Distributed algorithm
	bash $INSTANCEPATH/instance-$ins-dis.sh $ans > $RESULTPATH/instance-$ins-dis-$ans.log 2>&1
    done
done