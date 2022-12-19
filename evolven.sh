#!/usr/bin/env bash

SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &> /dev/null && pwd)
ROOT_DIR=$(dirname ${SCRIPT_DIR})

jar_file=$(ls ${ROOT_DIR}/lib/evolven-cli*.jar)
if [[ ! -f ${jar_file} ]]; then
  jar_file=$(ls ${SCRIPT_DIR}/build/output/lib/evolven-cli*.jar)
fi
if [[ ! -f ${jar_file} ]]; then
  echo Could not find Evolven CLI launcher.
  exit 1
fi

java -jar ${jar_file} "$@"
