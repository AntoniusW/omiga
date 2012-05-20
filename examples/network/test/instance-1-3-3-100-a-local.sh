#!/bin/bash

# start this file from WOC base directory
export BASEDIR=.
export CLASSPATH=$BASEDIR/dist/woc.jar:$BASEDIR/lib/antlr-3.4-complete.jar:$BASEDIR/lib/jgrapht-0.8.3/jgrapht-jdk1.6.jar
export INPUTFILE=$BASEDIR/examples/network/test/instance-1-3-3-100-a-mixed.lp

time java -cp $CLASSPATH woc.classinstances.main_FINAL $INPUTFILE $1
