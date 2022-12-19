#!/usr/bin/env bash

SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &> /dev/null && pwd)
ROOT_DIR=$(dirname ${SCRIPT_DIR})
JAR_FILE_PATTERN='evolven-cli*.jar'

JAR_FILE=$(ls ${ROOT_DIR}/lib/${JAR_FILE_PATTERN})
if [[ ! -f ${jar_file} ]]; then
  JAR_FILE=$(ls ${SCRIPT_DIR}/build/output/lib/${JAR_FILE_PATTERN})
fi
if [[ ! -f ${JAR_FILE} ]]; then
  echo Could not find Evolven CLI launcher.
  exit 1
fi

java -jar ${JAR_FILE} "$@"
