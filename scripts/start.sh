#!/usr/bin/env bash
txtred='\033[1;31m'
txtlw='\033[1;33m'
txtpur='\033[1;35m'
txtgrn='\033[1;32m'
txtgrey='\033[1;30m'

ABSPATH = $(readlink -f $0)
ABSDIR = $(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ubuntu/

echo -e "${txtpur} >> 디렉토리 이동"
cd $REPOSITORY/no-stop/zip/api-module/build/libs/

JAR_NAME=$(ls -t | grep jar | tail -n 1)
echo -e "${txtred} >> JAR_NAME: $JAR_NAME"

echo -e "${txtred} >> $JAR_NAME에 실행 권한 추가"
chmod +x $JAR_NAME

echo -e "${txtred} >> $JAR_NAME 실행"



echo -e "${txtred} >> $JAR_NAME를 profile=$IDLE_PROFILE 로 실행"


 nohup java -jar -Dspring.profiles.active=IDLE_PROFILE ${JAR_NAME} > $REPOSITORY/nohup.out 2>&1 &
  echo -e "${txtgrn}============finish deploying!========$(pgrep -f java)"