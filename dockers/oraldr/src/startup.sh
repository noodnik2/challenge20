#! /bin/bash

export ORACLE_DIR="/home/oracle"
export STARTUP_LOG="/tmp/dockerInitStartupLog"

startWebServer() {

  # "su - oracle" fails prior to database initialization
  while ! grep "The database is ready for use" "$STARTUP_LOG"; do
    echo "Waiting for database to finish initialization..."
    sleep 30
  done

  echo "Starting Web Server..."
  su - oracle -c "cd webserver; python -m CGIHTTPServer"

}

echo "Start of $STARTUP_LOG"> "$STARTUP_LOG"
startWebServer &

echo "Starting Oracle Server..."
"$ORACLE_DIR"/setup/dockerInit.sh | tee -a "$STARTUP_LOG"

# notreached
echo "Shutting Down..."
