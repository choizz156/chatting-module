#!/usr/bin/env bash
txtred='\033[1;31m'
txtlw='\033[1;33m'
txtpur='\033[1;35m'
txtgrn='\033[1;32m'
txtgrey='\033[1;30m'

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)

echo -e "${txtpur} >> $IDLE_PORT에서 구동 중인 pid 확인"
IDLE_PID=$(lsof -i :${IDLE_PORT} -t)

if [ -z ${IDLE_PID}  ]; then
  echo -e "${txtgrn} >> 해당 포트에서 구동 중인 애플리케이션이 없습니다."
else
  echo -e "${txtgrn} >> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 3
fi

