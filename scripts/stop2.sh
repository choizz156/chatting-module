#!/usr/bin/env bash

txtred='\033[1;31m'
txtlw='\033[1;33m'
txtpur='\033[1;35m'
txtgrn='\033[1;32m'
txtgrey='\033[1;30m'

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PROFILE=$(curl -s https://choizz-chat.r-e.kr/profile)
echo -e "${txtpur} 연결된 포트: $IDLE_PROFILE"

if [ ${IDLE_PROFILE} == prod1 ]
then
    IDLE_PORT=8082
else
    IDLE_PORT=8081
fi

IDLE_PID=$(lsof -i :${IDLE_PORT} -t)
echo -e "${txtpur} 해제할 pid: $IDLE_PID"
if [ -z ${IDLE_PID} ]; then
  echo -e "${txtgrn} >> 해당 포트에서 구동 중인 애플리케이션이 없습니다."
else
  echo -e "${txtgrn} >> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 3
  echo -e "${txtpur} 구동 종료"
fi