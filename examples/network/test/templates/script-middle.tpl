export SLEEP=5
export PIDFILE=nodes.pid

# start all nodes
> $PIDFILE      # empty pidfile

# start registry
echo "Starting registry."
rmiregistry -J-Djava.class.path=$CLASSPATH &
echo $! > $PIDFILE

echo "Starting nodes now ..."
