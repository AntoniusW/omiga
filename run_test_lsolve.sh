#!/bin/bash

declare -i TIMEOUT=600
declare -i MEMOUT=1000

export GNUTIME="/usr/bin/time --verbose -o" # time command
export TIMELIMIT="timelimit -p -s 1 -t $TIMEOUT -T 20"
export TIMEFORMAT=$'\nreal\t%3R\nuser\t%3U\nsys\t%3S' # time format

export BASEDIR=..
export CLASSPATH=$BASEDIR/build/classes/:$BASEDIR/lib/antlr-3.4-complete.jar:$BASEDIR/lib/jgrapht-0.8.3/jgrapht-jdk1.6.jar


export ASPERIXPATH='../../ASPeRiX0.2.4'
export SICSTUSPATH='/home/staff/aweinz/sicstus4.2.1/bin'
export CLINGOPATH='/home/staff/aweinz/src/clingo/clingo-3.0.4-x86-linux'
export OMIGAPATH='.'
export INSTANCES='instances.txt'

export EMAIL=dao@kr.tuwien.ac.at

DOTIMELIMIT=no
ULIMIT=no

export TESTCASES="reach birds 3col"
#export TESTCASES="birds"

#                      reach     reachplus birds                           3col                           stratProg
#declare -a test_names=(1 2 3 4 5 1 2 3 4 5 1000 10000 100000 200000 500000 10-18 20-38 30-64 50-114 10 100 200 300 400 500)
#declare -a no_test_names=(5 5 5 5 6)
#declare -a start_test_names=(0 5 5 15 21)

#                     reach reachplus birds   3col   stratProg
#declare -a n_answers=(0     0         0       1 5   1 5)
#declare -a no_n_answers=(1 1 1 2 2)
#declare -a start_n_answers=(0 1 2 3 5)

#                      reach     birds                3col                 reachplus      stratProg
declare -a test_names=(1 2 3     1000 10000 100000    10-18 20-38 30-64    1 2 3          10 100 200)
declare -a no_test_names=(3 3 3 3 3)
declare -a start_test_names=(0 3 6 9 12)

#                     reach reachplus birds   3col   stratProg
declare -a n_answers=(0     0         0       1 5   1 5)
declare -a no_n_answers=(1 1 1 2 2)
declare -a start_n_answers=(0 1 2 3 5)


###########################################################################
export ASPERIX="$ASPERIXPATH/asperix"
export DLV="dlv"
export SICSTUS="$SICSTUSPATH/sicstus"
export CLINGO="$CLINGOPATH/clingo"
export OMIGA="java -Xms256m -Xmx1500m -cp $CLASSPATH woc.classinstances.main_FINAL_NormalASP"


######## run a test case with by an engine ###############################
#
# @param:
#
# $1: OUTPUT: output file name with engine specified
#
##########################################################################
function runTestWithEngine()
{
    LOG=$1

    echo $RUNCOMMAND

    LOGSTATS=$LOG.log
    LOGTIME=$LOG.time
    LOGOUT=$LOG.out
    date > $LOGSTATS

    if [ x$DOTIMELIMIT = xyes ] ; then
	RUNCOMMAND="$TIMELIMIT $RUNCOMMAND"
    fi

    RUNCOMMAND="$GNUTIME $LOGTIME $RUNCOMMAND"

    if [ x$ULIMIT = xyes ] ; then
	( ulimit -v $((MEMOUT*1024)) ; $RUNCOMMAND ) > $LOGOUT 2>&1 
    else
	$RUNCOMMAND > $LOGOUT 2>&1
    fi

    RETVALUE=$?

    echo >> $LOGSTATS

    if [ $RETVALUE = 0 -o $RETVALUE = 10 ] ; then
	echo "PASS: $INPUT" >> $LOGSTATS
	(echo "Test $LOG finished" ; echo ; cat $LOGTIME ; echo ; cat $LOGOUT) | mail -s "Test $LOG finisihed" $EMAIL
    else 
	echo "FAILED: $INPUT" >> $LOGSTATS
	(echo "Test $LOG FAILED" ; echo ; cat $LOGTIME ; echo ; cat $LOGOUT) | mail -s "Test $LOG FAILED" $EMAIL
    fi

    echo >> $LOGSTATS
    date >> $LOGSTATS
    cat $LOGSTATS    
}


######## run a test case ##################################################
#
# @param:
#
# $1: INPUT: input file name
# $2: N: number of answers requested
# $3: OUTPUT: output file name
#
##########################################################################
function runTest()
{
#    RUNCOMMAND="$ASPERIX n $N -N 500000 $INPUT-hide.txt"
#    runTestWithEngine $OUTPUT-asperix

    #RUNCOMMAND="$DLV $INPUT.txt -filter=nothing -n=$N"
    #runTestWithEngine $OUTPUT-dlv

#    RUNCOMMAND="$CLINGO $INPUT.txt -n $N -q"
#    runTestWithEngine $OUTPUT-clingo

    RUNCOMMAND="$OMIGA $INPUT.txt $N"
    runTestWithEngine $OUTPUT-omiga

    #RUNCOMMAND="$SICSTUS -l gasp_test.pl -- $INPUT.txt"
    #runTestWithEngine $OUTPUT-gasp
}

########## check GNUTIME ################################################

$GNUTIME /dev/null ls > /dev/null 2>&1
if [ $? != 0 ] ; then
    echo I need GNU time, please setup GNUTIME properly. Bailing out.
    exit 1
fi

##########################################################################

declare -i i=0
declare -i j=0
declare -i k=0

if [ x$1 != xappend ] ; then
    > $INSTANCES
fi

for TEST in $TESTCASES ; do
    echo $TEST
    
    declare -i start_now=${start_test_names[$i]}
    declare -i end_now=$start_now
    let "end_now += ${no_test_names[$i]}"

    for (( j=$start_now; j < $end_now; ++j )) ; do
	INPUT=input/$TEST/$TEST-${test_names[$j]}
	echo $INPUT
	echo output/$TEST/$TEST-${test_names[$j]} >> $INSTANCES

	# iterate over the number of answers to request
	declare -i start_case=${start_n_answers[$i]}
	declare -i end_case=$start_case
	let "end_case += ${no_n_answers[$i]}"

	for (( k=$start_case; k < $end_case; ++k )) ; do
	    declare -i N=${n_answers[$k]}
	    OUTPUT=output/$TEST/$TEST-${test_names[$j]}-$N
	    #echo $OUTPUT
	    #echo $N

	    runTest $INPUT $N $OUTPUT
	done
    done

    let "i += 1"
done
