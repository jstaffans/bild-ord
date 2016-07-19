#!/bin/sh
### BEGIN INIT INFO
# Provides:          bild-ord
# Required-Start:    $local_fs $remote_fs $network $syslog
# Required-Stop:     $local_fs $remote_fs $network $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# X-Interactive:     true
# Short-Description: Start/stop bild-ord server
### END INIT INFO

WORK_DIR="/opt/bild-ord"
JAR="bild-ord-current.jar"
USER="root"
DAEMON="/usr/bin/java"
DAEMON_ARGS="-server -jar $WORK_DIR/$JAR"

[ -f /etc/environment ] && . /etc/environment

start () {
  echo "Starting bild-ord..."
  if [ ! -f $WORK_DIR/pid ]; then
    start-stop-daemon --start --quiet --background \
      --make-pidfile --pidfile $WORK_DIR/bild-ord.pid --chuid "$USER" \
      --startas /bin/bash -- -c "exec env PORT=$PORT $DAEMON $DAEMON_ARGS > /var/log/bild-ord.log 2>&1"
  else
    echo "bild-ord is already running..."
  fi
}

stop () {
    echo "Stopping bild-ord..."
    start-stop-daemon --stop --pidfile $WORK_DIR/bild-ord.pid
    rm $WORK_DIR/bild-ord.pid
}

case $1 in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        stop
        start
    ;;
esac
