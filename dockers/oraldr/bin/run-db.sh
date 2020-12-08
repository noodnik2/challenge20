# /bin/bash

BASE_NAME=oraldr
IMAGE_NAME="${BASE_NAME}_image"
CONTAINER_NAME="${BASE_NAME}_ctr"

BIN_DIR=`dirname $0`
VOL_DIR=`cd $BIN_DIR/../vol; pwd`

EXTERNAL_INBOX_PATH="$VOL_DIR/inbox"
EXTERNAL_OUTBOX_PATH="$VOL_DIR/outbox"

[ -d "$EXTERNAL_INBOX_PATH" -a -d "$EXTERNAL_OUTBOX_PATH" ] || exit 1

docker run -it \
  --name "$CONTAINER_NAME" \
  --shm-size="4g" \
  --publish 1521:1521 \
  --publish 5500:5500 \
  --publish 8000:8000 \
  --volume "$EXTERNAL_INBOX_PATH:/home/oracle/inbox" \
  --volume "$EXTERNAL_OUTBOX_PATH:/home/oracle/outbox" \
  "$IMAGE_NAME" \
  "/home/oracle/startup.sh"

