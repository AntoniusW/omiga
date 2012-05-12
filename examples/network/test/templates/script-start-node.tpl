java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.ANodeImpl n{0} $NODE{0}_FILE &
echo $! >> $PIDFILE
