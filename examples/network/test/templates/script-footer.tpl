echo "Sleeping for " $SLEEP " sec."
sleep $SLEEP

# killing all processes
echo "Killing created processes."
cat $PIDFILE | xargs -t -L 1 kill
