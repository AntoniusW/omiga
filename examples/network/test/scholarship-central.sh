#!/bin/bash

# start this file from WOC base directory
export BASEDIR=.
export CLASSPATH=$BASEDIR/build/classes/:$BASEDIR/lib/antlr-3.4-complete.jar:$BASEDIR/lib/jgrapht-0.8.3/jgrapht-jdk1.6.jar
export POLICYFILE=$BASEDIR/examples/network/server.policy
export NODE1_FILE=$BASEDIR/examples/network/test/instance-1-2-2-100-a-org-n1.lp
export NODE2_FILE=$BASEDIR/examples/network/test/instance-1-2-2-100-a-provider-n2.lp
export NODE3_FILE=$BASEDIR/examples/network/test/instance-1-2-2-100-a-provider-n3.lp
export NODE4_FILE=$BASEDIR/examples/network/test/instance-1-2-2-100-a-student-n4.lp
export NODE5_FILE=$BASEDIR/examples/network/test/instance-1-2-2-100-a-student-n5.lp
export SLEEP=5
export PIDFILE=nodes.pid

# start all nodes
> $PIDFILE      # empty pidfile

# start registry
echo "Starting registry."
rmiregistry -J-Djava.class.path=$CLASSPATH &
echo $! > $PIDFILE

echo "Starting nodes now ..."
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n1 $NODE1_FILE n2:offer,n3:offer &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n2 $NODE2_FILE ok,wait_list,refuse,offer,has_wait,out,neg_offer,scholarship,different_student,n4:apply,n5:apply,n4:refuse,n5:refuse &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n3 $NODE3_FILE ok,wait_list,refuse,offer,has_wait,out,neg_offer,scholarship,different_student,n4:apply,n5:apply,n4:refuse,n5:refuse &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n4 $NODE4_FILE n2:scholarship,n3:scholarship,n2:offer,n3:offer,apply,pick,refuse &
echo $! >> $PIDFILE
java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n5 $NODE5_FILE n2:scholarship,n3:scholarship,n2:offer,n3:offer,apply,pick,refuse &
echo $! >> $PIDFILE
# give nodes time to start up and register
echo "Giving nodes time to start up," $SLEEP "sec."
sleep $SLEEP

# start main entry (controller or client)
echo "Starting main entry (controller or client)."
time java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.AController 5
echo "Sleeping for " $SLEEP " sec."
sleep $SLEEP

# killing all processes
echo "Killing created processes."
cat $PIDFILE | xargs -t -L 1 kill

echo "Final sleeping for refreshing"
sleep 10
