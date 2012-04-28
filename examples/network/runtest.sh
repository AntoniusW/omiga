#!/bin/bash

# start this file from WOC base directory
export BASEDIR=.
export CLASSPATH=$BASEDIR/build/classes/:$BASEDIR/lib/antlr-3.4-complete.jar
export POLICYFILE=$BASEDIR/examples/network/server.policy
export CTX1_FILE=$BASEDIR/examples/network/ctx1.asd
export CTX2_FILE=$BASEDIR/examples/network/ctx2.asd

# start registry
echo "Starting registry."
rmiregistry -J-Djava.class.path=$CLASSPATH &

# start all nodes
echo "Starting nodes."
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n1 $CTX1_FILE &> ctx1.log &
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n2 $CTX2_FILE &> ctx2.log &

# give nodes time to start up and register
sleep 5

# start controller
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.AController
