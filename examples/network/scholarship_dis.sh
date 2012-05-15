#!/bin/bash

# start this file from WOC base directory
export BASEDIR=.
export CLASSPATH=$BASEDIR/build/classes/:$BASEDIR/lib/antlr-3.4-complete.jar:$BASEDIR/lib/jgrapht-0.8.3/jgrapht-jdk1.6.jar
export POLICYFILE=$BASEDIR/examples/network/server.policy
export CTX1_FILE=$BASEDIR/examples/network/org_n1.lp
export CTX2_FILE=$BASEDIR/examples/network/student_n2.lp
export CTX3_FILE=$BASEDIR/examples/network/student_n3.lp

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
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 0 3 $CTX1_FILE &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 1 3 $CTX2_FILE &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 2 3 $CTX3_FILE &
echo $! >> $PIDFILE


# give nodes time to start up and register
echo "Giving discontroller time to start up," $STARTUP_WAIT "sec."
sleep $STARTUP_WAIT

# start clinet
echo "Starting client."
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.Client 3

echo "Sleeping for " $CONTROLLER_SLEEP " sec."
sleep $CONTROLLER_SLEEP

# killing all processes
echo "Killing created processes."
cat $PIDFILE | xargs -t -L 1 kill
