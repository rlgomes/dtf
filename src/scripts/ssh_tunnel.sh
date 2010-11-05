#!/bin/bash

COMMAND=$1
MACHINE=$2
RPORT=$3
SPORT=$4

if [ "$4" == "" ]
then
    # default dtfc port
    SPORT=20000	
fi

TUNNELFILE=tunnel.conf

# this is used because the FreeBSD ssh needs a command to run in background 
# otherwise it will just disconnect...
REMOTECMD="while [ true ]; do sleep 1; done"

if [ "$COMMAND" == "add" ]
then
    # start reverse ssh tunnel from other machine to the controller on this machine
    ssh -f -nNT -R $SPORT:127.0.0.1:$SPORT $MACHINE $REMOTECMD
    if [ $? != 0 ] 
    then
        exit $?
    fi
        
    # now start up the local tunnel to the remote machine for all communication with
    # that remote machine
    ssh -f -N -L $RPORT:127.0.0.1:$RPORT $MACHINE $REMOTECMD
    if [ $? != 0 ] 
    then
        exit $?
    fi
   
    echo "$MACHINE=$RPORT=$SPORT" >> $TUNNELFILE
    (
        while [ $? == 0 ] 
        do
            sleep 1
            grep $MACHINE $TUNNELFILE > /dev/null 2>&1
        done

        for P in `ps x | egrep "ssh.*:127.0.0.1:.*$MACHINE" | awk '{print $1}'`
        do
            kill -9 $P > /dev/null 2>&1 # don't want to see errors for now
        done

        echo "Tunnel removed for $MACHINE:$RPORT:$SPORT"
    ) &
    exit 0
fi

if [ "$COMMAND" == "refresh" ]
then
    TUNNELS=`cat $TUNNELFILE`
    rm $TUNNELFILE
    sleep 5
    for TUNNEL in $TUNNELS
    do
        HOST=`echo $TUNNEL | awk -F= '{print $1}'`
        RPORT=`echo $TUNNEL | awk -F= '{print $2}'`
        SPORT=`echo $TUNNEL | awk -F= '{print $3}'`
        
        echo "Refreshing $HOST:$RPORT:$SPORT"
        $0 add $HOST $RPORT $SPORT
    done
    exit 0
fi

if [ "$COMMAND" == "del" ]
then
    cat $TUNNELFILE | grep -v $MACHINE > tunnel.tmp
    cat tunnel.tmp > $TUNNELFILE
    rm tunnel.tmp
    echo "Removed any tunnel setup for $MACHINE"
    exit 0
fi

if [ "$COMMAND" == "list" ]
then
    echo "Currently active ssh tunnels:"
    echo "HOSTNAME = REMOTE PORT = DTFC LISTENING PORT"
    echo ""
    cat $TUNNELFILE
    exit 0
fi


echo "SSH Tunnel Help"
echo "***************"
echo "./ssh_tunnel.sh [command] machine_name port [dtfc listening port]"
echo "     command:          add, del, list or refresh"
echo ""
echo "     machine_name:     fully qualified hostname to create a tunnel to."
echo ""
echo "     port:             port to create the tunnel on this side, on the "
echo "                       agent side there is a reverse tunnel on pot 20000."
echo ""
echo "     dtfc_listen_port: the listening port for the dtfc (default 20000), " 
echo "                       but in some scenarios its useful to be able to "
echo "                       change this port. Remember to use the "
echo "                       -Ddtf.listen.port=xxxx on the DTFC side as well as"
echo "                       the -Ddtf.connect.port=xxxx on the DTFA side."
exit -1

