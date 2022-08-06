FROM        openjdk:8-jre-alpine

ARG         SOURCE_DIR=.
ARG         PREFIX=/opt/java

RUN         echo ${SOURCE_DIR}

COPY        ${SOURCE_DIR}/target/. ${PREFIX}/
COPY        ${SOURCE_DIR}/run.sh  ${PREFIX}/
COPY        ${SOURCE_DIR}/migrate  ${PREFIX}/migrate
COPY        ${SOURCE_DIR}/.env  ${PREFIX}/
COPY        ${SOURCE_DIR}/db.sh  ${PREFIX}/
COPY        ${SOURCE_DIR}/version ${PREFIX}/

RUN         chmod 755 ${PREFIX}/migrate/bin/migrate ${PREFIX}/run.sh ${PREFIX}/db.sh

RUN         sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
            && apk add --no-cache perl bash

WORKDIR     ${PREFIX}
CMD         ["sh", "./run.sh"]

EXPOSE      3000
