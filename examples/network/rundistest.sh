#!/bin/bash

# start this file from WOC base directory
export BASEDIR=.
export CLASSPATH=$BASEDIR/build/classes/:$BASEDIR/dist/lib/antlr-3.4-complete.jar:$BASEDIR/dist/lib/jgrapht-jdk1.6.jar
export POLICYFILE=$BASEDIR/examples/network/server.policy
export CTX1_FILE=$BASEDIR/examples/network/ctx1.asd
export CTX2_FILE=$BASEDIR/examples/network/ctx2.asd

export STARTUP_WAIT=6
export PIDFILE=nodes.pid

# start all nodes
> $PIDFILE      # empty pidfile

# start registry
echo "Starting registry."
rmiregistry -J-Djava.class.path=$CLASSPATH &
echo $! > $PIDFILE


echo "Starting nodes now..."
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n1 $CTX1_FILE &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n2 $CTX2_FILE &
echo $! >> $PIDFILE

# give nodes time to start up and register
echo "Giving nodes time to start up," $STARTUP_WAIT "sec."
sleep 6

# DisController's command-line arguments:
# args[0] = myid
# args[1] = system_size

echo "Starting distributed controllers now..."
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisController 0 2 & 
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisController 1 2 &
echo $! >> $PIDFILE


echo "Giving distributed controllers time to start up."
sleep 5

# Client's command-line 
echo "Starting the client"
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.Client 2 &
echo $! >> $PIDFILE

# killing all processes
echo "Killing created processes."
cat $PIDFILE | xargs -t -L 1 kill
