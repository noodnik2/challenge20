#! /bin/bash

IMAGE_NAME=oraldr_image
BIN_DIR=`dirname $0`

docker build "$BIN_DIR" --tag "$IMAGE_NAME"
