#!/bin/bash

ulimit -n 32000

# start this file from WOC base directory
export BASEDIR=.
export CLASSPATH=$BASEDIR/dist/woc.jar:$BASEDIR/lib/antlr-3.4-complete.jar:$BASEDIR/lib/jgrapht-0.8.3/jgrapht-jdk1.6.jar
export POLICYFILE=$BASEDIR/examples/network/server.policy
export NODE1_FILE=$BASEDIR/examples/network/test/instance-1-3-3-100-a-org-n1.lp
export NODE2_FILE=$BASEDIR/examples/network/test/instance-1-3-3-100-a-provider-n2.lp
export NODE3_FILE=$BASEDIR/examples/network/test/instance-1-3-3-100-a-provider-n3.lp
export NODE4_FILE=$BASEDIR/examples/network/test/instance-1-3-3-100-a-provider-n4.lp
export NODE5_FILE=$BASEDIR/examples/network/test/instance-1-3-3-100-a-student-n5.lp
export NODE6_FILE=$BASEDIR/examples/network/test/instance-1-3-3-100-a-student-n6.lp
export NODE7_FILE=$BASEDIR/examples/network/test/instance-1-3-3-100-a-student-n7.lp
export SLEEP=5
export PIDFILE=nodes.pid

# start all nodes
> $PIDFILE      # empty pidfile

# start registry
echo "Starting registry."
rmiregistry -J-Djava.class.path=$CLASSPATH &
echo $! > $PIDFILE

echo "Starting nodes now ..."
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 0 7 $NODE1_FILE &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 1 7 $NODE2_FILE &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 2 7 $NODE3_FILE &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 3 7 $NODE4_FILE &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 4 7 $NODE5_FILE &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 5 7 $NODE6_FILE &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl 6 7 $NODE7_FILE &
echo $! >> $PIDFILE
# give nodes time to start up and register
echo "Giving nodes time to start up," $SLEEP "sec."
sleep $SLEEP

# start main entry (controller or client)
echo "Starting main entry (controller or client)."
time java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.Client 7 $1
echo "Sleeping for " $SLEEP " sec."
sleep $SLEEP

# killing all processes
echo "Killing created processes."
cat $PIDFILE | xargs -t -L 1 kill

echo "Final sleeping for refreshing"
sleep 10
