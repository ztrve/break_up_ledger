# !/bin/sh

trap 'kill -TERM $PID'  TERM INT

# Run migration first
echo "Running migrations ..."
for i in $(seq 1 30); do
    cd /opt/java/ || exit
    # migrate
    ./db.sh up > /dev/null 2>&1
    [ $? = 0 ] && break

    echo "Reconnecting db $i ..." && sleep 1
done

[ $? != 0 ] && echo "Failed to connect db, aborted!" && sleep 1 && exit 1

echo "Starting services ..."


# Uncomment below lines if the SVC is the owner of DB
#echo "Running migrations ..."
#for i in $(seq 1 30); do
#    ./migrations/bin/migrate up > /dev/null 2>&1
#    [ $? = 0 ] && break
#    echo "Reconnecting db $i ..." && sleep 1
#done
#
#[ $? != 0 ] && echo "Failed to connect db, aborted!" && sleep 1 && exit 1

# Import extra option if exist
[ -f javaopt.conf ] && source javaopt.conf

MSMEM=${MSMEM:-256m}
MXMEM=${MXMEM:-512m}
# Set it in javaopt.conf if needed
# CLASS=${ENTRY_CLASS:-com.linktopa.app.main}

export VERSION=`cat version`_$REV

echo "Starting service [$VERSION] ..."
java ${JAVA_OPTS} -Xms$MSMEM -Xmx$MXMEM \
        -Djava.security.egd=file:/dev/./urandom \
        -Duser.timezone=GMT+8 \
        -Dspring.profiles.active=prod \
        -Dglobal.config.path=/opt/java \
        -jar `ls /opt/java/*-*.jar` $CLASS

tail -f /dev/null &
PID=$!
# Now blocking ...
wait ${PID}

# Here a TERM/INT signal must be received
trap - TERM INT
echo "Service exited!"
