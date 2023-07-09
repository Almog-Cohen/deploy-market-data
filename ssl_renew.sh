#!/bin/bash

COMPOSE="/usr/local/bin/docker-compose --ansi never"
DOCKER="/usr/bin/docker"

cd /home/user/market-data/
 run certbot renew --dry-run &&  kill -s SIGHUP webserver
 system prune -af
