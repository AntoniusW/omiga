java -cp $CLASSPATH -Djava.security.policy=$POLICYFILE network.DisControllerImp {1} {2} $NODE{0}_FILE &
echo $! >> $PIDFILE
