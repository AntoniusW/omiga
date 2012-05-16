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
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n1 $CTX1_FILE ok,wait_list,refuse,offer,has_wait,neg_offer,n2:apply,n3:apply,n2:refuse,n3:refuse,out,second_offer,scholarship,different &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n2 $CTX2_FILE n1:offer,apply,pick,refuse,n1:scholarship &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n3 $CTX3_FILE n1:offer,apply,pick,refuse,n1:scholarship &
echo $! >> $PIDFILE


# give nodes time to start up and register
echo "Giving nodes time to start up," $STARTUP_WAIT "sec."
sleep $STARTUP_WAIT

# start controller
echo "Starting controller."
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.AController 3

echo "Sleeping for " $CONTROLLER_SLEEP " sec."
sleep $CONTROLLER_SLEEP

# killing all processes
echo "Killing created processes."
cat $PIDFILE | xargs -t -L 1 kill
