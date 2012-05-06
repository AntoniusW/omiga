#!/bin/bash

# start this file from WOC base directory
export BASEDIR=.
export CLASSPATH=$BASEDIR/build/classes/:$BASEDIR/lib/antlr-3.4-complete.jar:$BASEDIR/lib/jgrapht-0.8.3/jgrapht-jdk1.6.jar
export POLICYFILE=$BASEDIR/examples/network/server.policy
export CTX1_FILE=$BASEDIR/examples/network/diamond1.lp
export CTX2_FILE=$BASEDIR/examples/network/diamond2.lp
export CTX3_FILE=$BASEDIR/examples/network/diamond3.lp
export CTX4_FILE=$BASEDIR/examples/network/diamond4.lp

export STARTUP_WAIT=6
export CONTROLLER_SLEEP=5
export PIDFILE=nodes.pid

# start all nodes
> $PIDFILE      # empty pidfile

# start registry
echo "Starting registry."
rmiregistry -J-Djava.class.path=$CLASSPATH &
echo $! > $PIDFILE


echo "Starting nodes now ..."
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n1 $CTX1_FILE &
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n2 $CTX2_FILE &
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n3 $CTX3_FILE &
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n4 $CTX4_FILE &
echo $! >> $PIDFILE


# give nodes time to start up and register
echo "Giving nodes time to start up," $STARTUP_WAIT "sec."
sleep $STARTUP_WAIT

# start controller
echo "Starting controller."
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.AController 4

echo "Sleeping for " $CONTROLLER_SLEEP " sec."
sleep $CONTROLLER_SLEEP

# killing all processes
echo "Killing created processes."
cat $PIDFILE | xargs -t -L 1 kill
