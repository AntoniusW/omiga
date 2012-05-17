#!/bin/bash

# start this file from WOC base directory
export BASEDIR=.
export CLASSPATH=$BASEDIR/build/classes/:$BASEDIR/lib/antlr-3.4-complete.jar:$BASEDIR/lib/jgrapht-0.8.3/jgrapht-jdk1.6.jar
export INPUTFILE=$BASEDIR/examples/network/test/instance-1-2-2-100-a-mixed.lp

time java -cp $CLASSPATH woc.classinstances.main_FINAL $INPUTFILE
