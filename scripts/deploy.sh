#!/bin/bash
txtred='\033[1;31m'
txtlw='\033[1;33m'
txtpur='\033[1;35m'
txtgrn='\033[1;32m'
txtgrey='\033[1;30m'

REPOSITORY=/home/ubuntu/

echo -e "${txtpur} >> 디렉토리 이동"
cd /home/ubuntu/deploy/zip/api-module/build/libs/

JAR_NAME=$(ls -t | grep jar | tail -n 1)
echo -e "${txtred} >> JAR_NAME: $JAR_NAME"

echo -e "${txtpur} >>>구동 중인 pid 확인"

CURRENT_PID=$(pgrep -fl api-module | grep jar | awk '{print $1}')

echo -e "${txtred} >>>>구동 중인 애플리케이션 pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo -e "${txtgrn} >> 구동 중인 애플리케이션이 없습니다."
else
  echo -e "${txtgrn} >> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 3
fi

echo -e "${txtpur} 새 애플리케이션 배포"


echo -e "${txtred} >> $JAR_NAME에 실행 권한 추가"
chmod +x $JAR_NAME

echo -e "${txtred} >> $JAR_NAME 실행"

 nohup java -jar -Dspring.profiles.active=prod ${JAR_NAME} > $REPOSITORY/nohup.out 2>&1 &
  echo -e "${txtgrn}============finish deploying!========$(pgrep -f java)"