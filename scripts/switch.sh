#!/usr/bin/env bash
txtred='\033[1;31m'
txtlw='\033[1;33m'
txtpur='\033[1;35m'
txtgrn='\033[1;32m'
txtgrey='\033[1;30m'

ABSPATH = $(readlink -f $0)
ABSDIR = $(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy(){
  IDLE_PORT=$(find_idle_port)

  echo -e "${txtpur} 전환할 port: $IDLE_PORT"
  echo -e "${txtpur} port 전환"
  echo -e "set \$service_url http://172.17.0.1:${IDLE_PORT};" |
  sudo tee /etc/nginx/conf.d/service-url.inc

  echo -e "${txtpur} nginx reload"

  NGINX_CONTAINER = "reverse_proxy"

  docker exec ${NGINX_CONTAINER} nginx -s reload
}

