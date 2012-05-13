java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImpl {1} {2} $NODE{0}_FILE &
echo $! >> $PIDFILE
