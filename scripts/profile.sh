#!/usr/bin/env bash
txtred='\033[1;31m'
txtlw='\033[1;33m'
txtpur='\033[1;35m'
txtgrn='\033[1;32m'
txtgrey='\033[1;30m'

function find_idle_profile(){
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" https://choizz-chat.r-e.kr/profile)

  if [ ${RESPONSE_CODE} -ge 400 ]
  then
    CURRENT_PROFILE=prod2
  else
    CURRENT_PROFILE=$(curl -s https://choizz-chat.r-e.kr/profile)
  fi

  if [ ${CURRENT_PROFILE} == prod1 ]
  then
    IDLE_PROFILE=prod2
  else
    IDLE_PROFILE=prod1
  fi

  echo "${IDLE_PROFILE}"
}

function find_idle_port(){
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == prod1 ]
  then
    echo "8081"
  else
    echo "8082"
  fi
}